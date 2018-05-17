package com.arjunalabs.android.xkcdreader.repository

import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface XKCDService {

    @GET("{number}/info.0.json ")
    fun getComicByNumber(@Path("number") number: String): Observable<XkcdData>

    @GET("info.0.json ")
    fun getLatestComic() : Observable<XkcdData>

    companion object {
        fun create() : XKCDService {
            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("https://xkcd.com/")
                    .build()

            return retrofit.create(XKCDService::class.java)
        }
    }
}