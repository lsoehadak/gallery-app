package com.lsoehadak.galleryapp.home

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lsoehadak.galleryapp.data.ImageEntity
import com.lsoehadak.galleryapp.databinding.FragmentHomeBinding
import com.lsoehadak.galleryapp.detail.DetailImageActivity
import com.lsoehadak.galleryapp.utils.api.RetrofitClient
import com.lsoehadak.galleryapp.utils.ext.*
import java.util.*

class HomeFragment : Fragment() {

    private lateinit var fragmentHomeBinding: FragmentHomeBinding

    private lateinit var imageAdapter: ImageAdapter
    private var imageList = arrayListOf<ImageEntity?>()
    private var isLoading = true
    private var totalPage = 1
    private var page = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return fragmentHomeBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var timer = Timer()
        fragmentHomeBinding.etSearchImage.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                timer.cancel()

                val delay = 700L
                timer = Timer()
                timer.schedule(object : TimerTask() {
                    override fun run() {
                        activity?.runOnUiThread {
                            if (s.toString().isEmpty()) {
                                initGetImages()
                            } else {
                                if (s.toString().length > 3) {
                                    initSearchImages(s.toString())
                                }
                            }
                        }
                    }
                }, delay)
            }
        })

        setupRecyclerView()

        initGetImages()
    }

    private fun setupRecyclerView() {
        with(fragmentHomeBinding.rvImage) {
            imageAdapter = ImageAdapter(
                imageList,
                onItemSelectedListener = {
                    val intent = Intent(context, DetailImageActivity::class.java).apply {
                        putExtra(DetailImageActivity.EXTRA_IMAGE_ENTITY, it)
                    }
                    startActivity(intent)
                })
            adapter = imageAdapter

            val layoutManager = GridLayoutManager(context, 2)
            this.layoutManager = layoutManager

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    val visibleItemCount = layoutManager.childCount
                    val pastVisibleItemCount = layoutManager.findFirstVisibleItemPosition()
                    val totalItem = layoutManager.itemCount
                    if (!isLoading && page < totalPage) {
                        if (visibleItemCount + pastVisibleItemCount >= totalItem) {
                            page++
                            isLoading = true

                            val searchBarText = fragmentHomeBinding.etSearchImage.text.toString()
                            if (searchBarText.isEmpty())
                                getImages()
                            else
                                searchImages(searchBarText)
                        }
                    }

                    super.onScrolled(recyclerView, dx, dy)
                }
            })
        }
    }

    private fun initGetImages() {
        imageList.clear()
        page = 1

        fragmentHomeBinding.rvImage.visibility = View.GONE
        fragmentHomeBinding.progressBar.visibility = View.VISIBLE

        getImages()
    }

    private fun initSearchImages(keyword: String) {
        Log.e("search for", keyword)
        imageList.clear()
        page = 1

        fragmentHomeBinding.rvImage.visibility = View.GONE
        fragmentHomeBinding.progressBar.visibility = View.VISIBLE

        searchImages(keyword)
    }

    private fun getImages() {
        val queryMap = hashMapOf(
            "page" to page,
            "limit" to 15
        )

        RetrofitClient.service().getImages(
            queryMap = queryMap
        ).doEnqueue(
            onRequestSuccess = {
                if (activity != null) {
                    if (it.isSuccessful) {
                        totalPage = it.getPaginationData().getInt("total_pages")
                        val iiifUrl = it.getConfigData().getIiifUrl()

                        imageAdapter.removeNullData()
                        val dataArr = it.getDataArr()

                        val newImages = arrayListOf<ImageEntity>()
                        for (data in dataArr) {
                            val dataObj = data.asJsonObject
                            val imageEntity = dataObj.getImageEntity(iiifUrl)
                            newImages.add(imageEntity)
                        }

                        imageAdapter.addNewData(newImages)

                        if (page < totalPage) {
                            imageAdapter.addNullData()
                            isLoading = false
                        }

                        fragmentHomeBinding.rvImage.visibility = View.VISIBLE
                        fragmentHomeBinding.progressBar.visibility = View.GONE
                    }
                }
            }, onRequestFailure = {
                // TODO on failure handle
                if (activity != null) {

                }
            }
        )
    }

    private val searchImageResult = arrayListOf<ImageEntity>()
    private fun searchImages(keyword: String) {
        fragmentHomeBinding.etSearchImage.isEnabled = false
        searchImageResult.clear()

        val queryMap = hashMapOf(
            "q" to keyword,
            "page" to page.toString(),
            "limit" to 15.toString()
        )

        RetrofitClient.service().searchImages(
            queryMap = queryMap
        ).doEnqueue(
            onRequestSuccess = {
                if (activity != null) {
                    if (it.isSuccessful) {
                        totalPage = it.getPaginationData().getInt("total_pages")

                        val dataArr = it.getDataArr()

                        val ids = arrayListOf<String>()
                        for (data in dataArr) {
                            val dataObj = data.asJsonObject
                            ids.add(dataObj.getString("id"))
                        }

                        getImagesInfo(ids, 0)
                    }
                }
            }, onRequestFailure = {
                // TODO on failure handle
                if (activity != null) {

                }
            }
        )
    }

    private fun getImagesInfo(ids: List<String>, index: Int) {
        RetrofitClient.service().getImageInfo(
            id = ids[index]
        ).doEnqueue(
            onRequestSuccess = {
                if (activity != null) {
                    if (it.isSuccessful) {
                        val dataObj = it.getDataObj()
                        val iiifUrl = it.getConfigData().getIiifUrl()
                        searchImageResult.add(dataObj.getImageEntity(iiifUrl))

                        if (index < ids.size - 1) {
                            getImagesInfo(ids, index + 1)
                        } else {
//                            Log.e("total index", ids.size.toString())
//                            Log.e("page", page.toString())
                            imageAdapter.removeNullData()

                            imageAdapter.addNewData(searchImageResult)

                            if (page < totalPage) {
                                imageAdapter.addNullData()
                                isLoading = false
                            }

                            fragmentHomeBinding.etSearchImage.isEnabled = true
                            fragmentHomeBinding.rvImage.visibility = View.VISIBLE
                            fragmentHomeBinding.progressBar.visibility = View.GONE
                        }
                    }
                }
            }, onRequestFailure = {
                // TODO on failure handle
                if (activity != null) {

                }
            }
        )
    }
}