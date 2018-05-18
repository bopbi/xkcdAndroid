package com.arjunalabs.android.xkcdreader.usecase

import io.reactivex.Observable

interface GetComicByNumber {

    fun execute(numberString: String) : Observable<GetComicResult>
}