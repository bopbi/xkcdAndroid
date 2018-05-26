package com.arjunalabs.android.xkcdreader.ui

import android.arch.lifecycle.ViewModel
import com.arjunalabs.android.xkcdreader.ui.state.MainActivityState
import com.arjunalabs.android.xkcdreader.usecase.GetComicByNumber
import com.arjunalabs.android.xkcdreader.usecase.GetComicResult
import com.arjunalabs.android.xkcdreader.usecase.GetLatestComic
import com.arjunalabs.android.xkcdreader.usecase.GetLatestComicResult
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
    private val compositeDisposable = CompositeDisposable()
    private var latestNum: Int = 0
    private var currentNum: Int = 0

    init {
        behaviorSubject.onNext(MainActivityState.Uninitialized)
    }

    fun loadLatest() {
        compositeDisposable.add(getLatestComic.execute()
                .subscribeOn(subscriberSchedulers)
                .observeOn(observerSchedulers)
                .map {
                    when (it) {
                        is GetLatestComicResult.Error -> MainActivityState.Error("error")
                        is GetLatestComicResult.Loading -> MainActivityState.Loading
                        is GetLatestComicResult.Success -> MainActivityState.Data(it.data, prevButtonEnabled = true)
                    }
                }
                .subscribe {
                    // add reducer here
                    if (it is MainActivityState.Data) {
                        latestNum = it.data.num
                        currentNum = latestNum
                    }

            behaviorSubject.onNext(it)
        })
    }

    private fun getComicByNumber(numberString : String) {
        compositeDisposable.add(getComicByNumber.execute(numberString)
                .subscribeOn(subscriberSchedulers)
                .observeOn(observerSchedulers)
                .map {
                    when (it) {
                        is GetComicResult.Error -> MainActivityState.Error("error")
                        is GetComicResult.Loading -> MainActivityState.Loading
                        is GetComicResult.Success -> {
                            val state = MainActivityState.Data(it.data)
                            when (it.data.num) {
                                latestNum -> {
                                    state.prevButtonEnabled = true
                                    state.nextButtonEnabled = false
                                }
                                0 -> {
                                    state.prevButtonEnabled = false
                                    state.nextButtonEnabled = true
                                }
                                else -> {
                                    state.prevButtonEnabled = true
                                    state.nextButtonEnabled = true
                                }
                            }
                            state
                        }
                    }
                }
                .subscribe {
                    if (it is MainActivityState.Data) {
                        currentNum = it.data.num
                    }
                    behaviorSubject.onNext(it)
                })
    }

    fun nextComic() {
        val index = currentNum + 1
        getComicByNumber(index.toString())
    }

    fun prevComic() {
        val index = currentNum - 1
        getComicByNumber(index.toString())
    }

    fun getState() : Observable<MainActivityState> = behaviorSubject

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}