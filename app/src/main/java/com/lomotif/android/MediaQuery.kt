package com.lomotif.android

import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import com.lomotif.android.model.Video
import java.util.concurrent.TimeUnit


class MediaQuery(private val context: Context) {
    private val count = 0
    private var cursor: Cursor? = null
    lateinit var mediaItem: MutableList<Video>
    var listMedia = ArrayList<Video>()
    val videoFormatSupport = arrayOf(
        ".mp4",
        ".flv",
        ".m3u8",
        ".ts",
        ".3gp",
        ".mov",
        ".avi",
        ".wmv",
        ".mkv"
    )


    fun getAllMedia(): ArrayList<Video> {
        val selection: String? = null
        val videoProjection = arrayOf(
            MediaStore.Video.VideoColumns._ID,
            MediaStore.Video.VideoColumns.TITLE,
            MediaStore.Video.VideoColumns.DATA,
            MediaStore.Video.VideoColumns.DISPLAY_NAME,
            MediaStore.Video.VideoColumns.DURATION
        )

        val audioProjection = arrayOf(
            MediaStore.Audio.AudioColumns._ID,
            MediaStore.Audio.AudioColumns.TITLE,
            MediaStore.Audio.AudioColumns.DATA,
            MediaStore.Audio.AudioColumns.DISPLAY_NAME,
            MediaStore.Audio.AudioColumns.DURATION
        )

        cursor = context.contentResolver.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            videoProjection,
            selection, null, null
        )

        listMedia.addAll(addMediaItem(cursor!!))


        cursor = context.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            audioProjection,
            selection, null, null
        )

        listMedia.addAll(addMediaItem(cursor!!))


        return listMedia as ArrayList<Video>

    }

    private fun addMediaItem(cursor: Cursor): ArrayList<Video> {
        mediaItem = ArrayList()
        var videoItem: Video
        while (cursor.moveToNext()) {
            videoItem = Video()
            videoItem.id = cursor.getString(0)
            videoItem.title = cursor.getString(1)
            videoItem.data = cursor.getString(2)
            videoItem.display_name = cursor.getString(3)
            videoItem.duration = cursor.getString(4)
            mediaItem.add(videoItem)
        }
        return mediaItem as ArrayList<Video>
    }

//    fun getAllAudio(): ArrayList<Video> {
//        val selection: String? = null
//        val projection = arrayOf(
//            MediaStore.Audio.AudioColumns._ID,
//            MediaStore.Audio.AudioColumns.TITLE,
//            MediaStore.Audio.AudioColumns.DATA,
//            MediaStore.Audio.AudioColumns.DISPLAY_NAME,
//            MediaStore.Audio.AudioColumns.DURATION
//        )
//        cursor = context.contentResolver.query(
//            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
//            projection,
//            selection, null, null
//        )
//        mediaItem = ArrayList()
//        var videoItem: Video
//        while (cursor!!.moveToNext()) {
//            videoItem = Video()
//            videoItem.id = cursor!!.getString(0)
//            videoItem.title = cursor!!.getString(1)
//            videoItem.data = cursor!!.getString(2)
//            videoItem.display_name = cursor!!.getString(3)
//            videoItem.duration = cursor!!.getString(4)
//            mediaItem.add(videoItem)
//        }
//        return mediaItem as ArrayList<Video>
//    }

    fun convertTime(millis: Long): String {
        val hms = String.format(
            "%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
            TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(
                TimeUnit.MILLISECONDS.toHours(
                    millis
                )
            ),
            TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(
                TimeUnit.MILLISECONDS.toMinutes(
                    millis
                )
            )
        )
        println(hms)
        return hms
    }

    val videoCount: Int
        get() {

            return mediaItem.size
        }
}