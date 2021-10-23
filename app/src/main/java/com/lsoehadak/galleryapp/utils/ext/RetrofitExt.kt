package com.lsoehadak.galleryapp.utils.ext

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

fun <T> Call<T>.doEnqueue(
    onRequestSuccess: (Response<T>) -> Unit,
    onRequestFailure: (t: Throwable?) -> Unit
) {
    this.enqueue(object : Callback<T> {
        override fun onFailure(call: Call<T>?, t: Throwable?) {
            onRequestFailure(t)
        }

        override fun onResponse(call: Call<T>?, response: Response<T>) {
            onRequestSuccess(response)
        }
    })
}
