package com.lomotif.android.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ImageGallery : Serializable {
    @SerializedName("totalHits")
    var totalHits : Int? = null
    @SerializedName("hits")
    var hits : List<Hit>? = null
    @SerializedName("total")
    var total : Int? = null
}