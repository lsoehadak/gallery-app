package com.lsoehadak.galleryapp.saved

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.lsoehadak.galleryapp.data.ImageDB
import com.lsoehadak.galleryapp.data.ImageEntity
import com.lsoehadak.galleryapp.databinding.FragmentSavedImageBinding
import com.lsoehadak.galleryapp.detail.DetailImageActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SavedImageFragment : Fragment() {

    private lateinit var fragmentSavedImageBinding: FragmentSavedImageBinding

    private val db by lazy { ImageDB(requireContext()) }

    private lateinit var savedImageAdapter: SavedImageAdapter
    private val savedImages = arrayListOf<ImageEntity>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentSavedImageBinding =
            FragmentSavedImageBinding.inflate(layoutInflater, container, false)
        return fragmentSavedImageBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        getSavedImages()
    }

    private fun setupRecyclerView() {
        with(fragmentSavedImageBinding.rvSavedImage) {
            savedImageAdapter = SavedImageAdapter(
                savedImages,
                onItemSelectedListener = {
                    val intent = Intent(context, DetailImageActivity::class.java).apply {
                        putExtra(DetailImageActivity.EXTRA_IMAGE_ENTITY, it)
                    }
                    startActivity(intent)
                },
                onItemRemovedListener = {
                    removeImage(it)
                }
            )
            adapter = savedImageAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun removeImage(image:ImageEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            db.imageDao().removeImage(image)
        }
    }

    private fun getSavedImages() {
        savedImageAdapter.clearData()
        CoroutineScope(Dispatchers.IO).launch {
            val savedImages = db.imageDao().getSavedImages()
            withContext(Dispatchers.Main) {
                savedImageAdapter.addNewData(savedImages)
            }
        }
    }
}