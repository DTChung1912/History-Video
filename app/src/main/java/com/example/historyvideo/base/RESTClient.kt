package com.example.historyvideo.base

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RESTClient {
    private var retrofit: Retrofit? = null

    fun getClient(url: String?): Retrofit? {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit
    }
}
