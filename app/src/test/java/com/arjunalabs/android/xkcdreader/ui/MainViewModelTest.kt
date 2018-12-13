package com.arjunalabs.android.xkcdreader.ui

import com.arjunalabs.android.xkcdreader.repository.XkcdData
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
import org.junit.Test

class MainViewModelTest {

    private val getComicByNumber: GetComicByNumber = mock()
    private val getLatestComic: GetLatestComic = mock()
    private val scheduler = Schedulers.trampoline()
    private val viewModel = MainViewModel(getComicByNumber, getLatestComic, scheduler, scheduler)
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

        val testSubscriber = viewModel.getObservableState().test()

        viewModel.loadLatest()

        Assert.assertTrue(testSubscriber.values()[0] === MainActivityState.Uninitialized)
        Assert.assertTrue(testSubscriber.values()[1] is MainActivityState.Error)
    }

    @Test
    fun loadLatest_whenGetLatestReturnLoadingResult_shouldTriggerObservableLoadingState() {

        whenever(getLatestComic.execute())
                .thenReturn(Observable.just(GetLatestComicResult.Loading))

        val testSubscriber = viewModel.getObservableState().test()

        viewModel.loadLatest()

        Assert.assertTrue(testSubscriber.values()[0] === MainActivityState.Uninitialized)
        Assert.assertTrue(testSubscriber.values()[1] === MainActivityState.Loading)
    }

    @Test
    fun loadLatest_whenGetLatestReturnSuccessResult_shouldTriggerObservableDataState() {

        whenever(getLatestComic.execute())
                .thenReturn(Observable.just(GetLatestComicResult.Success(dataResult)))

        val testSubscriber = viewModel.getObservableState().test()

        viewModel.loadLatest()

        Assert.assertTrue(testSubscriber.values()[0] === MainActivityState.Uninitialized)
        Assert.assertEquals(testSubscriber.values()[1], MainActivityState.Data(dataResult, true, false))
    }

    @Test
    fun nextComic_whenGetComicByNumberReturnErrorResult_shouldTriggerObservableErrorState() {

        whenever(getComicByNumber.execute(any()))
                .thenReturn(Observable.just(GetComicResult.Error("")))

        val testSubscriber = viewModel.getObservableState().test()

        viewModel.nextComic()

        Assert.assertTrue(testSubscriber.values()[0] === MainActivityState.Uninitialized)
        Assert.assertTrue(testSubscriber.values()[1] is MainActivityState.Error)
    }

    @Test
    fun nextComic_whenGetComicByNumberReturnLoadingResult_shouldTriggerObservableLoadingState() {

        whenever(getComicByNumber.execute(any()))
                .thenReturn(Observable.just(GetComicResult.Loading))

        val testSubscriber = viewModel.getObservableState().test()

        viewModel.nextComic()

        Assert.assertTrue(testSubscriber.values()[0] === MainActivityState.Uninitialized)
        Assert.assertTrue(testSubscriber.values()[1] === MainActivityState.Loading)
    }

    @Test
    fun nextComic_whenGetComicByNumberReturnSuccessResult_shouldTriggerObservableDataState() {

        whenever(getComicByNumber.execute(any()))
                .thenReturn(Observable.just(GetComicResult.Success(dataResult)))

        val testSubscriber = viewModel.getObservableState().test()

        viewModel.nextComic()

        Assert.assertTrue(testSubscriber.values()[0] === MainActivityState.Uninitialized)
        Assert.assertEquals(testSubscriber.values()[1], MainActivityState.Data(dataResult, true, true))
    }

    @Test
    fun prevComic_whenGetComicByNumberReturnErrorResult_shouldTriggerObservableErrorState() {

        whenever(getComicByNumber.execute(any()))
                .thenReturn(Observable.just(GetComicResult.Error("")))

        val testSubscriber = viewModel.getObservableState().test()

        viewModel.prevComic()

        Assert.assertTrue(testSubscriber.values()[0] === MainActivityState.Uninitialized)
        Assert.assertTrue(testSubscriber.values()[1] is MainActivityState.Error)
    }

    @Test
    fun prevComic_whenGetComicByNumberReturnLoadingResult_shouldTriggerObservableLoadingState() {

        whenever(getComicByNumber.execute(any()))
                .thenReturn(Observable.just(GetComicResult.Loading))

        val testSubscriber = viewModel.getObservableState().test()

        viewModel.prevComic()

        Assert.assertTrue(testSubscriber.values()[0] === MainActivityState.Uninitialized)
        Assert.assertTrue(testSubscriber.values()[1] === MainActivityState.Loading)
    }

    @Test
    fun prevComic_whenGetComicByNumberReturnSuccessResult_shouldTriggerObservableDataState() {

        whenever(getComicByNumber.execute(any()))
                .thenReturn(Observable.just(GetComicResult.Success(dataResult)))

        val testSubscriber = viewModel.getObservableState().test()

        viewModel.prevComic()

        Assert.assertTrue(testSubscriber.values()[0] === MainActivityState.Uninitialized)
        Assert.assertEquals(testSubscriber.values()[1], MainActivityState.Data(dataResult, true, true))
    }
}
