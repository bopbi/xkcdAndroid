package com.arjunalabs.android.xkcdreader.usecase

import io.reactivex.Observable

interface GetLatestComic {
    fun execute() : Observable<GetLatestComicResult>
}