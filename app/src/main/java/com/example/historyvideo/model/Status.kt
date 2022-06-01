package com.example.historyvideo.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Status : Serializable {
    @SerializedName("code")
    var code = 0

    @SerializedName("user_message")
    var userMessage: String? = null

    @SerializedName("message_type")
    var messageType: String? = null
}