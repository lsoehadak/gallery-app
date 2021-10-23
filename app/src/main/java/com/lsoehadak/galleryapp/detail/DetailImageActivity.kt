package com.lsoehadak.galleryapp.detail

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.lsoehadak.galleryapp.R
import com.lsoehadak.galleryapp.data.ImageEntity
import com.lsoehadak.galleryapp.databinding.ActivityDetailImageBinding

class DetailImageActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_IMAGE_ENTITIY = "image_entity"
    }

    private lateinit var activityDetailImageBinding: ActivityDetailImageBinding
    private lateinit var image: ImageEntity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityDetailImageBinding = ActivityDetailImageBinding.inflate(layoutInflater)
        setContentView(activityDetailImageBinding.root)

        image = intent.getParcelableExtra(EXTRA_IMAGE_ENTITIY)!!

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
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.m_save_unsave -> {

            }
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}