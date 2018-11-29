package com.arjunalabs.android.xkcdreader.ui

import android.widget.Button
import android.widget.ImageView
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.arjunalabs.android.xkcdreader.R
import org.hamcrest.Matchers.notNullValue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    val rule = ActivityTestRule<MainActivity>(MainActivity::class.java)

    @Test
    fun mainActivity_shouldHave_imageViewPrevNextButton() {
        val activity = rule.activity

        val imageView = activity.findViewById<ImageView>(R.id.imageview_main)
        val prevButton = activity.findViewById<Button>(R.id.button_prev)
        val nextButton = activity.findViewById<Button>(R.id.button_next)

        assertThat(imageView, notNullValue())
        assertThat(prevButton, notNullValue())
        assertThat(nextButton, notNullValue())
    }
}