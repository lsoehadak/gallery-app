package com.lsoehadak.galleryapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ImageDao {

    @Insert
    fun addImage(image: ImageEntity)

    @Delete
    fun removeImage(image: ImageEntity)

    @Query("SELECT * FROM imageEntity")
    fun getSavedImages(): List<ImageEntity>

    @Query("SELECT * FROM imageEntity WHERE id=:id")
    fun getImageInfo(id: String): ImageEntity

    @Query("SELECT EXISTS(SELECT * FROM imageEntity WHERE id=:id)")
    fun isImageSaved(id: String): Boolean

}