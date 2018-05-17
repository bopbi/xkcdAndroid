package com.arjunalabs.android.xkcdreader.ui

import android.arch.lifecycle.ViewModel
import com.arjunalabs.android.xkcdreader.ui.state.MainActivityState
import com.arjunalabs.android.xkcdreader.usecase.GetComicByNumber
import com.arjunalabs.android.xkcdreader.usecase.GetLatestComic
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

class MainViewModel(
        private val getComicByNumber: GetComicByNumber,
        private val getLatestComic: GetLatestComic,
        private val subscriberSchedulers: Scheduler = Schedulers.io(),
        private val observerSchedulers: Scheduler = AndroidSchedulers.mainThread()) : ViewModel() {

    private val behaviorSubject = BehaviorSubject.create<MainActivityState>()
    private var currentState = MainActivityState()
    private val compositeDisposable = CompositeDisposable()

    init {
        behaviorSubject.onNext(currentState)
    }

    fun loadLatest() {
        compositeDisposable.add(getLatestComic.execute()
                .subscribeOn(subscriberSchedulers)
                .observeOn(observerSchedulers)
                .subscribe {
                    currentState = MainActivityState(true, it)
                    // add reducer here

            behaviorSubject.onNext(currentState)
        })
    }

    private fun getComicByNumber(numberString : String) {
        compositeDisposable.add(getComicByNumber.execute(numberString)
                .subscribeOn(subscriberSchedulers)
                .observeOn(observerSchedulers)
                .subscribe {
                    currentState = MainActivityState(true, it)
                    // add reducer here

                    behaviorSubject.onNext(currentState)
                })
    }

    fun nextComic() {
        currentState.data?.let {
            val index = it.num + 1
            getComicByNumber(index.toString())
        }
    }

    fun prevComic() {
        currentState.data?.let {
            val index = it.num - 1
            getComicByNumber(index.toString())
        }
    }

    fun getState() : Observable<MainActivityState> = behaviorSubject

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}