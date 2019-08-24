package com.lomotif.android

import com.lomotif.android.Model.ImageGallery

import retrofit2.Call
import retrofit2.http.GET


interface ApiInterface {

    companion object {
        var BASE_URL = "https://pixabay.com/api/"
    }
    @GET("?key=10961674-bf47eb00b05f514cdd08f6e11&page=1")
    fun getImageGallery(): Call<ImageGallery>

}