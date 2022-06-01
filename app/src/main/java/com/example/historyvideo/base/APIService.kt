package com.example.historyvideo.base

import com.example.historyvideo.model.LoginStatus
import io.reactivex.Single
import retrofit2.http.GET

interface APIService {
    @GET("api/v1/check_login_status")
    fun checkLoginStatus(): Single<APIResponse<LoginStatus?>?>?
}