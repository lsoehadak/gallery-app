package com.lsoehadak.galleryapp.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ImageEntity(
    val id: String,
    val url: String,
    val title: String,
    val inscriptions: String?,
    val creditLine: String?,
    val publicationHistory: String?,
    val exhibitionHistory: String?,
    val provenanceText: String?
) : Parcelable