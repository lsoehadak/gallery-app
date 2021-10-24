package com.lsoehadak.galleryapp.detail

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.lsoehadak.galleryapp.R
import com.lsoehadak.galleryapp.data.ImageDB
import com.lsoehadak.galleryapp.data.ImageEntity
import com.lsoehadak.galleryapp.databinding.ActivityDetailImageBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailImageActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_IMAGE_ENTITY = "image_entity"
    }

    private val db by lazy { ImageDB(this) }
    private var isImageSaved: Boolean = false
    private lateinit var actionMenu: Menu

    private lateinit var activityDetailImageBinding: ActivityDetailImageBinding
    private lateinit var image: ImageEntity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityDetailImageBinding = ActivityDetailImageBinding.inflate(layoutInflater)
        setContentView(activityDetailImageBinding.root)

        image = intent.getParcelableExtra(EXTRA_IMAGE_ENTITY)!!

        with(activityDetailImageBinding) {
            setSupportActionBar(toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowTitleEnabled(false)

            Glide.with(this@DetailImageActivity)
                .load(image.url)
                .apply(
                    RequestOptions.placeholderOf(R.drawable.ic_loading)
                        .error(R.drawable.ic_error)
                ).into(activityDetailImageBinding.ivImage)

            tvTitle.text = image.title
            tvCreditLine.text = "Credit: ${image.creditLine}"
            tvInscription.text = image.inscriptions
            tvProvenanceText.text = image.provenanceText
            tvPublicationHistory.text = image.publicationHistory
            tvExhibitionHistory.text = image.exhibitionHistory
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detail_image, menu)
        actionMenu = menu!!
        getImageSavedStatus()
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.m_save_unsave -> {
                if (isImageSaved) {
                    CoroutineScope(Dispatchers.IO).launch {
                        db.imageDao().removeImage(image)
                        isImageSaved = false
                        withContext(Dispatchers.Main) {
                            actionMenu.getItem(0).icon = ContextCompat.getDrawable(
                                this@DetailImageActivity,
                                R.drawable.ic_save
                            )
                        }
                    }
                } else {
                    CoroutineScope(Dispatchers.IO).launch {
                        db.imageDao().addImage(image)
                        isImageSaved = true
                        withContext(Dispatchers.Main) {
                            actionMenu.getItem(0).icon = ContextCompat.getDrawable(
                                this@DetailImageActivity,
                                R.drawable.ic_saved
                            )
                        }
                    }
                }
            }
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getImageSavedStatus() {
        CoroutineScope(Dispatchers.IO).launch {
            isImageSaved = db.imageDao().isImageSaved(image.id)
            withContext(Dispatchers.Main) {
                if (isImageSaved) {
                    actionMenu.getItem(0).icon = ContextCompat.getDrawable(
                        this@DetailImageActivity,
                        R.drawable.ic_saved
                    )
                } else {
                    actionMenu.getItem(0).icon = ContextCompat.getDrawable(
                        this@DetailImageActivity,
                        R.drawable.ic_save
                    )
                }
            }
        }
    }
}