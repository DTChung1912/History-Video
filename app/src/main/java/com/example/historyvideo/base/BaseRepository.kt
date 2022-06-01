package com.example.historyvideo.base

import retrofit2.create


class BaseRepository {
    protected lateinit var apiService : APIService

    init {
        var api : APIService = RESTClient().getClient("")?.create(APIService::class.java)!!
    }
}