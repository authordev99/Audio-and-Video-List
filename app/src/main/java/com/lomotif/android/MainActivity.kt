package com.lomotif.android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.lomotif.android.utils.CustomTabLayout
import com.lomotif.android.utils.SectionsPagerAdapter


class MainActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager
    private lateinit var adapter: SectionsPagerAdapter
    private lateinit var tabLayout: CustomTabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewPager = findViewById(R.id.viewPager)
        tabLayout = findViewById(R.id.custom_tab_layout)

        adapter = SectionsPagerAdapter(supportFragmentManager)
        adapter.add("Image", ImageFragment.newInstance())
        adapter.add("Video & Audio", VideoAudioFragment.newInstance())
        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)

        (viewPager.adapter as SectionsPagerAdapter).notifyDataSetChanged()

    }


}
