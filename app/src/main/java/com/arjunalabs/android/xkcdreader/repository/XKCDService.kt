package com.arjunalabs.android.xkcdreader.repository

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

interface XKCDService {

    @GET("{number}/info.0.json ")
    fun getComicByNumber(@Path("number") number: String): Observable<XkcdData>

    @GET("info.0.json ")
    fun getLatestComic(): Observable<XkcdData>
}