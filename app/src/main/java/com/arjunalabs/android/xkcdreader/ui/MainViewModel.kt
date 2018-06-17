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
                nextButtonEnabled = false,
                currentNum = -1,
                latestNum = -1
        )
    }

   fun loadLatest() {
        compositeDisposable.add(getLatestComic.execute()
                .subscribeOn(subscriberSchedulers)
                .observeOn(observerSchedulers)
                .subscribe {
                    val currentState = state.value
                    val result = it
                    currentState?.let {
                        when (result) {
                            is GetLatestComicResult.Error -> {
                                state.value = MainActivityState(
                                        loading = currentState.loading,
                                        uninitialized = false,
                                        error = true,
                                        data = currentState.data,
                                        prevButtonEnabled = currentState.prevButtonEnabled,
                                        nextButtonEnabled = currentState.nextButtonEnabled,
                                        currentNum = currentState.currentNum,
                                        latestNum = currentState.latestNum
                                )
                            }
                            is GetLatestComicResult.Loading -> {
                                state.value = MainActivityState(
                                        loading = true,
                                        uninitialized = false,
                                        error = false,
                                        data = currentState.data,
                                        prevButtonEnabled = currentState.prevButtonEnabled,
                                        nextButtonEnabled = currentState.nextButtonEnabled,
                                        currentNum = currentState.currentNum,
                                        latestNum = currentState.latestNum
                                )
                            }
                            is GetLatestComicResult.Success -> {
                                state.value = MainActivityState(
                                        loading = false,
                                        uninitialized = false,
                                        error = false,
                                        data = result.data,
                                        prevButtonEnabled = true,
                                        nextButtonEnabled = false,
                                        currentNum = result.data.num,
                                        latestNum = result.data.num
                                )
                            }
                        }

                    }
                })
    }

    private fun getComicByNumber(numberString: String) {
        compositeDisposable.add(getComicByNumber.execute(numberString)
                .subscribeOn(subscriberSchedulers)
                .observeOn(observerSchedulers)
                .subscribe {
                    val currentState = state.value
                    val result = it
                    currentState?.let {
                        when (result) {
                            is GetComicResult.Error -> {
                                state.value = MainActivityState(
                                        loading = currentState.loading,
                                        uninitialized = false,
                                        error = true,
                                        data = currentState.data,
                                        prevButtonEnabled = currentState.prevButtonEnabled,
                                        nextButtonEnabled = currentState.nextButtonEnabled,
                                        currentNum = currentState.currentNum,
                                        latestNum = currentState.latestNum
                                )
                            }
                            is GetComicResult.Loading -> {
                                state.value = MainActivityState(
                                        loading = true,
                                        uninitialized = false,
                                        error = false,
                                        data = currentState.data,
                                        prevButtonEnabled = currentState.prevButtonEnabled,
                                        nextButtonEnabled = currentState.nextButtonEnabled,
                                        currentNum = currentState.currentNum,
                                        latestNum = currentState.latestNum
                                )
                            }
                            is GetComicResult.Success -> {

                                val prevButtonEnabled = when (result.data.num) {
                                    (state.value?.latestNum ?: -1) -> {
                                        true
                                    }
                                    0 -> {
                                        false
                                    }
                                    else -> {
                                        true
                                    }
                                }

                                val nextButtonEnabled = when (result.data.num) {
                                    (state.value?.latestNum ?: -1) -> {
                                        false
                                    }
                                    0 -> {
                                        true
                                    }
                                    else -> {
                                        true
                                    }
                                }

                                state.value = MainActivityState(
                                        loading = false,
                                        uninitialized = false,
                                        error = false,
                                        data = result.data,
                                        prevButtonEnabled = prevButtonEnabled,
                                        nextButtonEnabled = nextButtonEnabled,
                                        currentNum = result.data.num,
                                        latestNum = state.value?.latestNum ?: -1
                                )
                            }
                        }
                    }

                })
    }

    fun nextComic() {
        state.value?.let {
            val index = it.currentNum + 1
            getComicByNumber(index.toString())
        }
    }

    fun prevComic() {
        state.value?.let {
            val index = it.currentNum - 1
            getComicByNumber(index.toString())
        }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}