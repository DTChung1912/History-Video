package com.example.historyvideo.base

import com.example.historyvideo.model.Status
import com.example.historyvideo.model.StatusCode
import com.google.gson.annotations.SerializedName

class APIResponse<T> {
    @SerializedName("status")
    val status: Status? = null

    @SerializedName("response")
    var response: T? = null
        private set

    fun setResponse(response: T) {
        this.response = response
    }

    val isSuccess: Boolean
        get() = status != null && status.code == StatusCode.API_SUCCESS
    val userMessage: String
        get() = if (status != null) status.userMessage.toString() else ""
}