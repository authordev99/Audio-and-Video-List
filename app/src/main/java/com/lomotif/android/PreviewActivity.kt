package com.lomotif.android

import android.app.DownloadManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.lomotif.android.utils.DownloadUtils

class PreviewActivity : AppCompatActivity() {

    lateinit var imageUrl: String
    private var downloadUtils: DownloadUtils? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        if (intent.hasExtra("url")) {
            imageUrl = intent.extras.getString("url")
        }

        val imageView = findViewById<ImageView>(R.id.ivPreview)
        Glide.with(this)
            .load(imageUrl)
            .asBitmap()
            .into(imageView)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_download, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {


        when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true

            }
            R.id.action_download_image -> {

                downloadUtils = DownloadUtils(
                    this,
                    getSystemService(DOWNLOAD_SERVICE) as DownloadManager,
                    imageUrl,
                    imageUrl.substring(imageUrl.lastIndexOf("/") + 1),
                    ""
                )
                downloadUtils!!.initialized()
                return true

            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        downloadUtils?.unRegisteredReceiver()
    }

}
