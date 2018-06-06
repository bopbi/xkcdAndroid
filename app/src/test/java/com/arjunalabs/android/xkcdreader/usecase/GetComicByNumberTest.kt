package com.arjunalabs.android.xkcdreader.usecase

import com.arjunalabs.android.xkcdreader.repository.XKCDService
import com.arjunalabs.android.xkcdreader.repository.XkcdData
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.reset
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class GetComicByNumberTest {

    private val xkcdService: XKCDService = mock()
    private val getComicByNumber = GetComicByNumberImpl(xkcdService)
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
        whenever(xkcdService.getComicByNumber(any())).thenReturn(Observable.error(Exception()))

        val testSubscriber = getComicByNumber.execute("").test()

        Assert.assertTrue(testSubscriber.values()[0] === GetComicResult.Loading)
        Assert.assertTrue(testSubscriber.values()[1] is GetComicResult.Error)
    }

    @Test
    fun execute_whenXkcdServiceReturnData_shouldReturnSuccessResult() {
        whenever(xkcdService.getComicByNumber(any())).thenReturn(Observable.just(dataResult))

        val testSubscriber = getComicByNumber.execute("").test()

        Assert.assertTrue(testSubscriber.values()[0] === GetComicResult.Loading)
        Assert.assertEquals(testSubscriber.values()[1], GetComicResult.Success(dataResult))
    }
}