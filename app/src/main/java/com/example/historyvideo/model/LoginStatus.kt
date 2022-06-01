package com.example.historyvideo.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class LoginStatus : Serializable {
    @SerializedName("title")
    var title: String? = null

    @SerializedName("body")
    var body: String? = null

    @SerializedName("os_type")
    val osType = 0
}