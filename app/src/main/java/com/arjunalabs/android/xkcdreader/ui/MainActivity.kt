package com.arjunalabs.android.xkcdreader.ui

import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import com.arjunalabs.android.xkcdreader.R
import com.arjunalabs.android.xkcdreader.repository.XKCDService
import com.arjunalabs.android.xkcdreader.usecase.GetComicByNumber
import com.arjunalabs.android.xkcdreader.usecase.GetLatestComic
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.kotlin.autoDisposable

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: MainViewModel

    private val scopeProvider by lazy { AndroidLifecycleScopeProvider.from(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val xkcdService = XKCDService.create()
        val getLatestComic = GetLatestComic(xkcdService)
        val getComicByNumber = GetComicByNumber(xkcdService)
        val imageView = findViewById<ImageView>(R.id.imageview_main)
        val prevButton = findViewById<Button>(R.id.button_prev)
        val nextButton = findViewById<Button>(R.id.button_next)
        viewModel = ViewModelProviders
                .of(this, MainViewModelFactory(getComicByNumber, getLatestComic))
                .get(MainViewModel::class.java)

        prevButton.setOnClickListener {
            viewModel.prevComic()
        }

        nextButton.setOnClickListener {
            viewModel.nextComic()
        }

        viewModel.getState()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .autoDisposable(scopeProvider)
                .subscribe {
                    Log.v(">XKCD APP", it.toString())
                    title = it.data?.title ?: ""

                    if (!it.isInitialized) {
                        viewModel.loadLatest()
                    } else {

                        prevButton.isEnabled = it.prevButtonEnabled
                        nextButton.isEnabled = it.nextButtonEnabled

                        it.data?.let {
                            Picasso.get().load(it.img).into(imageView)
                        }

                    }
                }
    }

}