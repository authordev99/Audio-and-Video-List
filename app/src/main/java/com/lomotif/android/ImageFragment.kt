package com.lomotif.android


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.ObservableArrayList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lomotif.android.Interface.ApiInterface
import com.lomotif.android.Interface.BinderHandler
import com.lomotif.android.Interface.ClickHandler
import com.lomotif.android.model.Hit
import com.lomotif.android.model.ImageGallery
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.lomotif.android.utils.RecyclerViewAdapter


/**
 * A simple [Fragment] subclass.
 */
class ImageFragment : Fragment(), BinderHandler<Any> {

    private lateinit var layout: View
    lateinit var imageGallery: ImageGallery
    var listHits = ObservableArrayList<Any>()
    lateinit var recyclerView: RecyclerView
    var isNextPageExist = false
    private var previousTotal = 0

    private val scrollListener: RecyclerView.OnScrollListener =
        object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                val visibleItemCount = recyclerView.childCount
                val totalItemCount = recyclerView.layoutManager!!.itemCount
                val manager = recyclerView.layoutManager as StaggeredGridLayoutManager

                var pastVisibleItems = 0
                var firstVisibleItems: IntArray? = null
                firstVisibleItems =
                    manager.findFirstVisibleItemPositions(firstVisibleItems)
                if (firstVisibleItems != null && firstVisibleItems.isNotEmpty()) {
                    pastVisibleItems = firstVisibleItems[0]
                }

                if (isNextPageExist) {
                    if (totalItemCount > previousTotal) {
                        isNextPageExist = false
                        previousTotal = totalItemCount
                    }
                }

                if (dy > 0) {
                    if (!isNextPageExist && (totalItemCount - visibleItemCount)
                        <= (pastVisibleItems + 10)
                    ) {
                        isNextPageExist = true
                        getImageGallery()
                    }
                }


            }

        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        layout = inflater.inflate(R.layout.recyclerview, container, false)
        recyclerView = layout.findViewById(R.id.recyclerView)
        recyclerView.addOnScrollListener(scrollListener)
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
                    listHits.addAll(imageGallery.hits!!)
                    println("list size = ${listHits.size}")

                    val layoutManager =
                        StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                    layoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
                    recyclerView.layoutManager = layoutManager
                    recyclerView.setHasFixedSize(true)
                    recyclerView.adapter =
                        RecyclerViewAdapter(requireContext(), listHits, clickHandler())
                    (recyclerView.adapter as RecyclerViewAdapter).notifyDataSetChanged()
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
