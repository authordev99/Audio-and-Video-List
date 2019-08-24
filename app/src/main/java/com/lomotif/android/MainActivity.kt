package com.lomotif.android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.lomotif.android.Model.ImageGallery
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getImageGallery()
    }

    private fun getImageGallery() {
        val retrofit = Retrofit.Builder()
                .baseUrl(ApiInterface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        val apiInterface = retrofit.create(ApiInterface::class.java)
        val getImageGallery = apiInterface.getImageGallery()

        getImageGallery.enqueue(object : Callback<ImageGallery> {
            override fun onFailure(call: Call<ImageGallery>, t: Throwable) {
                //can create custom error message handling to show meaningfull message to client
                Toast.makeText(this@MainActivity, "Unavailable", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<ImageGallery>, response: Response<ImageGallery>) {
                if (response.isSuccessful) {

                    println("response body = ${response.body()?.total}")


                }
            }
        })
    }

}
