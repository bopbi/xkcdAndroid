package com.arjunalabs.android.xkcdreader.ui

import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.arjunalabs.android.xkcdreader.R
import com.arjunalabs.android.xkcdreader.repository.XKCDService
import com.arjunalabs.android.xkcdreader.ui.state.MainActivityState
import com.arjunalabs.android.xkcdreader.usecase.GetComicByNumberImpl
import com.arjunalabs.android.xkcdreader.usecase.GetLatestComicImpl
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.kotlin.autoDisposable

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var imageView: ImageView
    private lateinit var prevButton: Button
    private lateinit var nextButton: Button

    private val scopeProvider by lazy { AndroidLifecycleScopeProvider.from(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val xkcdService = XKCDService.create()
        val getLatestComic = GetLatestComicImpl(xkcdService)
        val getComicByNumber = GetComicByNumberImpl(xkcdService)
        imageView = findViewById(R.id.imageview_main)
        prevButton = findViewById(R.id.button_prev)
        nextButton = findViewById(R.id.button_next)
        viewModel = ViewModelProviders
                .of(this, MainViewModelFactory(getComicByNumber, getLatestComic))
                .get(MainViewModel::class.java)

        prevButton.setOnClickListener {
            viewModel.prevComic()
        }

        nextButton.setOnClickListener {
            viewModel.nextComic()
        }

        viewModel.getObservableState()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .autoDisposable(scopeProvider)
                .subscribe {
                    Log.v(">XKCD APP", it.toString())

                    render(it)
                }
    }

    private fun render(mainActivityState: MainActivityState) {

        mainActivityState.let {

            when (it) {
                is  MainActivityState.Error -> {
                    Toast.makeText(this@MainActivity, it.errorString, Toast.LENGTH_LONG).show()
                }

                is MainActivityState.Loading -> {
                    Toast.makeText(this@MainActivity, "Loading", Toast.LENGTH_LONG).show()
                }

                is MainActivityState.Data -> {
                    prevButton.isEnabled = it.prevButtonEnabled
                    nextButton.isEnabled = it.nextButtonEnabled
                    Picasso.get().load(it.data.img).into(imageView)
                }

                is MainActivityState.Uninitialized -> {
                    viewModel.loadLatest()
                    prevButton.isEnabled = false
                    nextButton.isEnabled = false
                }
            }
        }
    }
}
