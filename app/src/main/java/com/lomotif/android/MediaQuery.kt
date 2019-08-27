package com.lomotif.android

import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import com.lomotif.android.model.Video


class MediaQuery(private val context: Context) {
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


        return listMedia

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


}