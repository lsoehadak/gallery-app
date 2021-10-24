package com.lsoehadak.galleryapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [ImageEntity::class],
    version = 1
)

abstract class ImageDB : RoomDatabase() {

    abstract fun imageDao(): ImageDao

    companion object {

        @Volatile
        private var instance: ImageDB? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            ImageDB::class.java,
            "galleryapp.db"
        ).build()
    }
}