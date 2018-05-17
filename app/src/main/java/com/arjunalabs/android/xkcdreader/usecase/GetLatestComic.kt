package com.arjunalabs.android.xkcdreader.usecase

import com.arjunalabs.android.xkcdreader.repository.XkcdData
import io.reactivex.Observable

interface GetLatestComic {
    fun execute() : Observable<XkcdData>
}