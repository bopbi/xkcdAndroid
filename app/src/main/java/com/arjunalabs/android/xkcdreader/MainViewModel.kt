package com.arjunalabs.android.xkcdreader

import android.arch.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

class MainViewModel(private val getComicByNumber: GetComicByNumber,
        private val getLatestComic: GetLatestComic,
        private val subscriberSchedulers: Scheduler = Schedulers.io(),
        private val observerSchedulers: Scheduler = AndroidSchedulers.mainThread()) : ViewModel() {

    private val behaviorSubject = BehaviorSubject.create<MainActivityState>()
    private var currentState = MainActivityState()

    init {
        behaviorSubject.onNext(currentState)
    }

    fun loadIfNotInitialized() {
        if (!currentState.isInitialized) {
            getLatestComic.execute()
                    .subscribeOn(subscriberSchedulers)
                    .observeOn(observerSchedulers)
                    .subscribe {
                        currentState = MainActivityState(true, it)
                        // add reducer here

                behaviorSubject.onNext(currentState)
            }
        }
    }

    private fun getComicByNumber(numberString : String) {
        getComicByNumber.execute(numberString)
                .subscribeOn(subscriberSchedulers)
                .observeOn(observerSchedulers)
                .subscribe {
                    currentState = MainActivityState(true, it)
                    // add reducer here

                    behaviorSubject.onNext(currentState)
                }
    }

    fun next() {
        currentState.data?.let {
            val index = it.num + 1
            getComicByNumber(index.toString())
        }
    }

    fun prev() {
        currentState.data?.let {
            val index = it.num - 1
            getComicByNumber(index.toString())
        }
    }

    fun getState() : Observable<MainActivityState> = behaviorSubject
}