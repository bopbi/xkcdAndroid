package com.arjunalabs.android.xkcdreader.ui

import androidx.lifecycle.ViewModel
import com.arjunalabs.android.xkcdreader.ui.state.MainActivityState
import com.arjunalabs.android.xkcdreader.usecase.GetComicByNumber
import com.arjunalabs.android.xkcdreader.usecase.GetComicResult
import com.arjunalabs.android.xkcdreader.usecase.GetLatestComic
import com.arjunalabs.android.xkcdreader.usecase.GetLatestComicResult
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject
import javax.inject.Named

class MainViewModel @Inject constructor(
        private val getComicByNumber: GetComicByNumber,
        private val getLatestComic: GetLatestComic,
        @Named("schedulersIo") private val subscriberSchedulers: Scheduler,
        @Named("schedulersMainThread")private val observerSchedulers: Scheduler
) : ViewModel(), StateObservable<MainActivityState> {

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
                        is GetLatestComicResult.Success -> MainActivityState.Data(it.data,
                                prevButtonEnabled = true)
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

    private fun getComicByNumber(numberString: String) {
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

    override fun getObservableState(): Observable<MainActivityState> = behaviorSubject

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}
