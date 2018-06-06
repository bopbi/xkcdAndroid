package com.arjunalabs.android.xkcdreader.ui

import io.reactivex.Observable

interface StateObservable<T> {

    fun getObservableState() : Observable<T>
}