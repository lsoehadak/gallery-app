package com.lsoehadak.galleryapp.utils.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    const val BASE_URL = "https://api.artic.edu/api/v1/"

    var retrofit: Retrofit? = null

    private fun getClient(): Retrofit? {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        return retrofit
    }

    fun service(): ApiInterface {
        return getClient()!!.create(ApiInterface::class.java)
    }
}