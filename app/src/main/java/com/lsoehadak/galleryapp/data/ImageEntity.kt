package com.lsoehadak.galleryapp.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class ImageEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val url: String,
    val title: String,
    val inscriptions: String?,
    val creditLine: String?,
    val publicationHistory: String?,
    val exhibitionHistory: String?,
    val provenanceText: String?
) : Parcelable