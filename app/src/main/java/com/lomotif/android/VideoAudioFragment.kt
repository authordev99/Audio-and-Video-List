package com.lomotif.android


import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ObservableArrayList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.lomotif.android.Interface.BinderHandler
import com.lomotif.android.Interface.ClickHandler
import com.lomotif.android.model.Video
import com.lomotif.android.utils.RecyclerViewAdapter
import java.util.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class VideoAudioFragment : Fragment(), BinderHandler<Any> {

    private lateinit var layout: View
    var listMedia = ObservableArrayList<Any>()
    lateinit var recyclerView: RecyclerView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        layout = inflater.inflate(R.layout.recyclerview, container, false)

        recyclerView = layout.findViewById(R.id.recyclerView)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            TedPermission(requireContext())
                .setDeniedMessage(R.string.permission_gallery_denied_msg)
                .setPermissions(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
                .setGotoSettingButton(true)
                .setPermissionListener(object : PermissionListener {
                    override fun onPermissionGranted() {

                        getAllMedia()
                    }

                    override fun onPermissionDenied(deniedPermissions: ArrayList<String>) {

                    }
                }).check()
        } else {
            getAllMedia()
        }

        return layout.rootView
    }

    fun getAllMedia() {
        listMedia.clear()
        listMedia.addAll(MediaQuery(requireContext()).getAllMedia())
        println("listAudio 2 = $listMedia")

        recyclerView.layoutManager =
            LinearLayoutManager(requireContext())
        recyclerView.adapter =
            RecyclerViewAdapter(
                requireContext(),
                listMedia,
                clickHandler()
            )
    }

    override fun clickHandler(): ClickHandler<Any> {
        return ClickHandler { item, _ ->
            val media = item as Video
            val intent = Intent(requireContext(), PreviewPlayerActivity::class.java)
            intent.putExtra(PreviewPlayerActivity.PARAM_URL, media.data)
            startActivity(intent)

        }
    }

    companion object {
        fun newInstance() = VideoAudioFragment()
    }

}
