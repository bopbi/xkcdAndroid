package com.arjunalabs.android.xkcdreader.ui

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.arjunalabs.android.xkcdreader.ui.state.MainActivityState
import com.arjunalabs.android.xkcdreader.usecase.GetComicByNumber
import com.arjunalabs.android.xkcdreader.usecase.GetComicResult
import com.arjunalabs.android.xkcdreader.usecase.GetLatestComic
import com.arjunalabs.android.xkcdreader.usecase.GetLatestComicResult
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainViewModel(
        private val getComicByNumber: GetComicByNumber,
        private val getLatestComic: GetLatestComic,
        private val subscriberSchedulers: Scheduler = Schedulers.io(),
        private val observerSchedulers: Scheduler = AndroidSchedulers.mainThread()) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private var latestNum: Int = 0
    private var currentNum: Int = 0

    private val state: MutableLiveData<MainActivityState> = MutableLiveData()

    val appState: LiveData<MainActivityState>
        get() = state

    init {
        state.value = MainActivityState(
                loading = false,
                uninitialized = true,
                error = false,
                data = null,
                prevButtonEnabled = false,
                nextButtonEnabled = false
        )
    }

   fun loadLatest() {
        compositeDisposable.add(getLatestComic.execute()
                .subscribeOn(subscriberSchedulers)
                .observeOn(observerSchedulers)
                .subscribe {
                    val newState = state.value
                    val result = it
                    newState?.let {
                        newState.uninitialized = false
                        when (result) {
                            is GetLatestComicResult.Error -> {
                                newState.error = true
                            }
                            is GetLatestComicResult.Loading -> {
                                newState.loading = true
                            }
                            is GetLatestComicResult.Success -> {
                                newState.data = result.data
                                newState.loading = false

                                latestNum = result.data.num
                                currentNum = latestNum

                                newState.prevButtonEnabled = true
                                newState.nextButtonEnabled = false
                            }
                        }

                        state.value = newState
                    }
                })
    }

    private fun getComicByNumber(numberString: String) {
        compositeDisposable.add(getComicByNumber.execute(numberString)
                .subscribeOn(subscriberSchedulers)
                .observeOn(observerSchedulers)
                .subscribe {
                    val newState = state.value
                    val result = it
                    newState?.let {
                        newState.uninitialized = false
                        when (result) {
                            is GetComicResult.Error -> {
                                newState.error = true
                            }
                            is GetComicResult.Loading -> {
                                newState.loading = true
                            }
                            is GetComicResult.Success -> {
                                newState.data = result.data
                                newState.loading = false

                                currentNum = result.data?.num
                                when (currentNum) {
                                    latestNum -> {
                                        newState.prevButtonEnabled = true
                                        newState.nextButtonEnabled = false
                                    }
                                    0 -> {
                                        newState.prevButtonEnabled = false
                                        newState.nextButtonEnabled = true
                                    }
                                    else -> {
                                        newState.prevButtonEnabled = true
                                        newState.nextButtonEnabled = true
                                    }
                                }
                            }
                        }
                        state.value = newState
                    }

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

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}