package com.arjunalabs.android.xkcdreader.ui

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.arjunalabs.android.xkcdreader.repository.XkcdData
import com.arjunalabs.android.xkcdreader.ui.MainViewModel
import com.arjunalabs.android.xkcdreader.ui.state.MainActivityState
import com.arjunalabs.android.xkcdreader.usecase.GetComicByNumber
import com.arjunalabs.android.xkcdreader.usecase.GetComicResult
import com.arjunalabs.android.xkcdreader.usecase.GetLatestComic
import com.arjunalabs.android.xkcdreader.usecase.GetLatestComicResult
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.reset
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class MainViewModelTest {

    @Rule
    @JvmField
    val rule: TestRule = InstantTaskExecutorRule()

    private val getComicByNumber: GetComicByNumber = mock()
    private val getLatestComic: GetLatestComic = mock()
    private val scheduler = Schedulers.trampoline()
    private val viewModel by lazy { MainViewModel(getComicByNumber, getLatestComic, scheduler, scheduler) }
    private val dataResult = XkcdData(
            month = 1,
            num = 3,
            link = "",
            year = 0,
            news = "",
            safe_title = "",
            transcript = "",
            alt = "",
            img = "",
            title = "",
            day = 1
    )

    @Before
    fun setup() {
        reset(getComicByNumber, getLatestComic)
    }

    @Test
    fun loadLatest_whenGetLatestReturnErrorResult_shouldTriggerObservableErrorState() {

        whenever(getLatestComic.execute())
                .thenReturn(Observable.just(GetLatestComicResult.Error("")))

        viewModel.loadLatest()

        Assert.assertNotNull(viewModel.appState.value)
        Assert.assertFalse(viewModel.appState.value!!.uninitialized)
        Assert.assertFalse(viewModel.appState.value!!.loading)
        Assert.assertTrue(viewModel.appState.value!!.error)
        Assert.assertNull(viewModel.appState.value!!.data)
        Assert.assertFalse(viewModel.appState.value!!.prevButtonEnabled)
        Assert.assertFalse(viewModel.appState.value!!.nextButtonEnabled)
    }

    @Test
    fun loadLatest_whenGetLatestReturnLoadingResult_shouldTriggerObservableLoadingState() {

        whenever(getLatestComic.execute())
                .thenReturn(Observable.just(GetLatestComicResult.Loading))

        viewModel.loadLatest()

        Assert.assertNotNull(viewModel.appState.value)
        Assert.assertFalse(viewModel.appState.value!!.uninitialized)
        Assert.assertTrue(viewModel.appState.value!!.loading)
        Assert.assertFalse(viewModel.appState.value!!.error)
        Assert.assertNull(viewModel.appState.value!!.data)
        Assert.assertFalse(viewModel.appState.value!!.prevButtonEnabled)
        Assert.assertFalse(viewModel.appState.value!!.nextButtonEnabled)
    }

    @Test
    fun loadLatest_whenGetLatestReturnSuccessResult_shouldTriggerObservableDataState() {

        whenever(getLatestComic.execute())
                .thenReturn(Observable.just(GetLatestComicResult.Success(dataResult)))

        viewModel.loadLatest()

        Assert.assertNotNull(viewModel.appState.value)
        Assert.assertFalse(viewModel.appState.value!!.uninitialized)
        Assert.assertFalse(viewModel.appState.value!!.loading)
        Assert.assertFalse(viewModel.appState.value!!.error)
        Assert.assertNotNull(viewModel.appState.value!!.data)
        Assert.assertTrue(viewModel.appState.value!!.prevButtonEnabled)
        Assert.assertFalse(viewModel.appState.value!!.nextButtonEnabled)
    }

    @Test
    fun nextComic_whenGetComicByNumberReturnErrorResult_shouldTriggerObservableErrorState() {

        whenever(getComicByNumber.execute(any()))
                .thenReturn(Observable.just(GetComicResult.Error("")))

        viewModel.nextComic()

        Assert.assertNotNull(viewModel.appState.value)
        Assert.assertFalse(viewModel.appState.value!!.uninitialized)
        Assert.assertFalse(viewModel.appState.value!!.loading)
        Assert.assertTrue(viewModel.appState.value!!.error)
        Assert.assertNull(viewModel.appState.value!!.data)
        Assert.assertFalse(viewModel.appState.value!!.prevButtonEnabled)
        Assert.assertFalse(viewModel.appState.value!!.nextButtonEnabled)
    }

    @Test
    fun nextComic_whenGetComicByNumberReturnLoadingResult_shouldTriggerObservableLoadingState() {

        whenever(getComicByNumber.execute(any()))
                .thenReturn(Observable.just(GetComicResult.Loading))

        viewModel.nextComic()

        Assert.assertNotNull(viewModel.appState.value)
        Assert.assertFalse(viewModel.appState.value!!.uninitialized)
        Assert.assertTrue(viewModel.appState.value!!.loading)
        Assert.assertFalse(viewModel.appState.value!!.error)
        Assert.assertNull(viewModel.appState.value!!.data)
        Assert.assertFalse(viewModel.appState.value!!.prevButtonEnabled)
        Assert.assertFalse(viewModel.appState.value!!.nextButtonEnabled)
    }

    @Test
    fun nextComic_whenGetComicByNumberReturnSuccessResult_shouldTriggerObservableDataState() {

        whenever(getComicByNumber.execute(any()))
                .thenReturn(Observable.just(GetComicResult.Success(dataResult)))

        viewModel.nextComic()

        Assert.assertNotNull(viewModel.appState.value)
        Assert.assertFalse(viewModel.appState.value!!.uninitialized)
        Assert.assertFalse(viewModel.appState.value!!.loading)
        Assert.assertFalse(viewModel.appState.value!!.error)
        Assert.assertNotNull(viewModel.appState.value!!.data)
        Assert.assertTrue(viewModel.appState.value!!.prevButtonEnabled)
        Assert.assertTrue(viewModel.appState.value!!.nextButtonEnabled)
    }

    @Test
    fun prevComic_whenGetComicByNumberReturnErrorResult_shouldTriggerObservableErrorState() {

        whenever(getComicByNumber.execute(any()))
                .thenReturn(Observable.just(GetComicResult.Error("")))

        viewModel.prevComic()

        Assert.assertNotNull(viewModel.appState.value)
        Assert.assertFalse(viewModel.appState.value!!.uninitialized)
        Assert.assertFalse(viewModel.appState.value!!.loading)
        Assert.assertTrue(viewModel.appState.value!!.error)
        Assert.assertNull(viewModel.appState.value!!.data)
        Assert.assertFalse(viewModel.appState.value!!.prevButtonEnabled)
        Assert.assertFalse(viewModel.appState.value!!.nextButtonEnabled)
    }

    @Test
    fun prevComic_whenGetComicByNumberReturnLoadingResult_shouldTriggerObservableLoadingState() {

        whenever(getComicByNumber.execute(any()))
                .thenReturn(Observable.just(GetComicResult.Loading))

        viewModel.prevComic()

        Assert.assertNotNull(viewModel.appState.value)
        Assert.assertFalse(viewModel.appState.value!!.uninitialized)
        Assert.assertTrue(viewModel.appState.value!!.loading)
        Assert.assertFalse(viewModel.appState.value!!.error)
        Assert.assertNull(viewModel.appState.value!!.data)
        Assert.assertFalse(viewModel.appState.value!!.prevButtonEnabled)
        Assert.assertFalse(viewModel.appState.value!!.nextButtonEnabled)
    }

    @Test
    fun prevComic_whenGetComicByNumberReturnSuccessResult_shouldTriggerObservableDataState() {

        whenever(getComicByNumber.execute(any()))
                .thenReturn(Observable.just(GetComicResult.Success(dataResult)))

        viewModel.prevComic()

        Assert.assertNotNull(viewModel.appState.value)
        Assert.assertFalse(viewModel.appState.value!!.uninitialized)
        Assert.assertFalse(viewModel.appState.value!!.loading)
        Assert.assertFalse(viewModel.appState.value!!.error)
        Assert.assertNotNull(viewModel.appState.value!!.data)
        Assert.assertTrue(viewModel.appState.value!!.prevButtonEnabled)
        Assert.assertTrue(viewModel.appState.value!!.nextButtonEnabled)
    }
}