package com.arjunalabs.android.xkcdreader.ui

import android.widget.Button
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.arjunalabs.android.xkcdreader.MyApplication
import com.arjunalabs.android.xkcdreader.R
import com.arjunalabs.android.xkcdreader.dagger.MyApplicationComponent
import com.arjunalabs.android.xkcdreader.dagger.MyApplicationModule
import com.arjunalabs.android.xkcdreader.repository.XKCDService
import com.arjunalabs.android.xkcdreader.repository.XkcdData
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable
import it.cosenonjaviste.daggermock.DaggerMock
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityInstrumentalTest {

    @get:Rule
    val daggerRule = DaggerMock.rule<MyApplicationComponent>(MyApplicationModule()) {
        set {
            val app = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as MyApplication
            app.myApplicationComponent = it
        }
    }

    @get:Rule
    var activityRule = ActivityTestRule(MainActivity::class.java, false, false)

    private val mockedXKCDService: XKCDService = mock()

    @Test
    fun mainActivity_shouldLoadMockedRetrofit() {
        whenever(mockedXKCDService.getLatestComic()).thenReturn(Observable.just(getXKCDLatestData()))
        whenever(mockedXKCDService.getComicByNumber("0")).thenReturn(Observable.just(getXKCDZeroData()))

        activityRule.launchActivity(null)

        val activity = activityRule.activity
        val prevButton = activity.findViewById<Button>(R.id.button_prev)
        val nextButton = activity.findViewById<Button>(R.id.button_next)

        // this should initialize the dataNum to 1
        assertThat(prevButton.isEnabled).isTrue()
        assertThat(nextButton.isEnabled).isFalse()

        // this should reduce the current xkcd data index to 0, disabling the prev button
        onView(withId(R.id.button_prev)).perform(click())
        assertThat(prevButton.isEnabled).isFalse()
        assertThat(nextButton.isEnabled).isTrue()
    }

    private fun getXKCDLatestData(): XkcdData = XkcdData(
            month = 1,
            num = 1,
            link = "link",
            year = 1,
            news = "news",
            safe_title = "safe_title",
            transcript = "transcript",
            alt = "alt",
            img = "blank image",
            title = "title",
            day = 1
    )

    private fun getXKCDZeroData(): XkcdData = XkcdData(
            month = 1,
            num = 0,
            link = "link",
            year = 1,
            news = "news",
            safe_title = "safe_title",
            transcript = "transcript",
            alt = "alt",
            img = "blank image",
            title = "title",
            day = 1
    )
}
