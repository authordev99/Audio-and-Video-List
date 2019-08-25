package com.lomotif.android


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import android.widget.Toast
import com.lomotif.android.Interface.ApiInterface
import com.lomotif.android.Interface.BinderHandler
import com.lomotif.android.Interface.ClickHandler
import com.lomotif.android.model.Hit
import com.lomotif.android.model.ImageGallery
import com.lomotif.android.utils.ImageAdapterGridView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * A simple [Fragment] subclass.
 */
class ImageFragment : Fragment(), BinderHandler<Any> {

    private lateinit var layout: View
    lateinit var imageGallery: ImageGallery
    lateinit var gridView: GridView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        layout = inflater.inflate(R.layout.fragment_image, container, false)
        gridView = layout.findViewById(R.id.gridView)
        getImageGallery()

        return layout.rootView
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
                Toast.makeText(requireContext(), "Unavailable", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<ImageGallery>, response: Response<ImageGallery>) {
                if (response.isSuccessful) {
                    println("response body = ${response.body()?.total}")
                    imageGallery = response.body()!!
                    gridView.adapter = ImageAdapterGridView(
                        requireContext(),
                        imageGallery.hits!!,
                        clickHandler()
                    )
                }
            }
        })
    }


    override fun clickHandler(): ClickHandler<Any> {
        return ClickHandler { item, _ ->
            val hit = item as Hit
            val intent = Intent(activity, PreviewActivity::class.java)
            intent.putExtra("url", hit.largeImageURL)
            startActivity(intent)

        }
    }


    companion object {
        fun newInstance() = ImageFragment()
    }

}
