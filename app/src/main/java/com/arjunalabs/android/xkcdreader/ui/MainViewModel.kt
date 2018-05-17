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
    private var latestNum: Int = 0

    init {
        behaviorSubject.onNext(currentState)
    }

    fun loadLatest() {
        compositeDisposable.add(getLatestComic.execute()
                .subscribeOn(subscriberSchedulers)
                .observeOn(observerSchedulers)
                .subscribe {
                    currentState = MainActivityState(true, nextButtonEnabled = false, prevButtonEnabled = true, data = it)
                    // add reducer here
                    latestNum = it.num

            behaviorSubject.onNext(currentState)
        })
    }

    private fun getComicByNumber(numberString : String) {
        compositeDisposable.add(getComicByNumber.execute(numberString)
                .subscribeOn(subscriberSchedulers)
                .observeOn(observerSchedulers)
                .subscribe {
                    currentState = MainActivityState(true, data = it)
                    // add reducer here
                    when (it.num) {
                        latestNum -> {
                            currentState.prevButtonEnabled = true
                            currentState.nextButtonEnabled = false
                        }
                        0 -> {
                            currentState.prevButtonEnabled = false
                            currentState.nextButtonEnabled = true
                        }
                        else -> {
                            currentState.prevButtonEnabled = true
                            currentState.nextButtonEnabled = true
                        }
                    }

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