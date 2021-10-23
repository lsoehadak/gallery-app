package com.lsoehadak.galleryapp.utils.api

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface ApiInterface {
    @GET("artworks")
    fun getImages(
        @QueryMap queryMap: Map<String, Int>
    ): Call<JsonObject>

    @GET("artworks/search")
    fun searchImages(
        @QueryMap queryMap: Map<String, String>
    ): Call<JsonObject>

    @GET("artworks/{identifier}")
    fun getImageInfo(
        @Path("identifier") id:String
    ): Call<JsonObject>
}