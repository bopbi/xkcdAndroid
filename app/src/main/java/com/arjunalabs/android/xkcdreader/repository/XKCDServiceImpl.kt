package com.arjunalabs.android.xkcdreader.repository

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class XKCDServiceImpl {
    companion object {
        fun create(): XKCDService {
            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("https://xkcd.com/")
                    .build()

            return retrofit.create(XKCDService::class.java)
        }
    }
}
