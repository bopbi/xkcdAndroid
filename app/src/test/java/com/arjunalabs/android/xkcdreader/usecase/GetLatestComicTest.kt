package com.arjunalabs.android.xkcdreader.usecase

import com.arjunalabs.android.xkcdreader.repository.XKCDService
import com.arjunalabs.android.xkcdreader.repository.XkcdData
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.reset
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class GetLatestComicTest {
    private val xkcdService: XKCDService = mock()
    private val getLatestComic = GetLatestComicImpl(xkcdService)
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
        reset(xkcdService)
    }

    @Test
    fun execute_whenXkcdServiceReturnError_shouldReturnErrorResult() {
        whenever(xkcdService.getLatestComic()).thenReturn(Observable.error(Exception()))

        val testSubscriber = getLatestComic.execute().test()

        Assert.assertTrue(testSubscriber.values()[0] === GetLatestComicResult.Loading)
        Assert.assertTrue(testSubscriber.values()[1] is GetLatestComicResult.Error)
    }

    @Test
    fun execute_whenXkcdServiceReturnData_shouldReturnSuccessResult() {
        whenever(xkcdService.getLatestComic()).thenReturn(Observable.just(dataResult))

        val testSubscriber = getLatestComic.execute().test()

        Assert.assertTrue(testSubscriber.values()[0] === GetLatestComicResult.Loading)
        Assert.assertEquals(testSubscriber.values()[1], GetLatestComicResult.Success(dataResult))
    }
}