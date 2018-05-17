package com.arjunalabs.android.xkcdreader.usecase

import com.arjunalabs.android.xkcdreader.repository.XkcdData
import io.reactivex.Observable

interface GetComicByNumber {

    fun execute(numberString: String) : Observable<XkcdData>
}