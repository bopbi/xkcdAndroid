package com.arjunalabs.android.xkcdreader.ui

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.arjunalabs.android.xkcdreader.R
import com.arjunalabs.android.xkcdreader.dagger.ViewModelFactory
import com.arjunalabs.android.xkcdreader.ui.state.MainActivityState
import com.squareup.picasso.Picasso
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.kotlin.autoDisposable
import dagger.android.AndroidInjection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var vmFactory: ViewModelFactory<MainViewModel>

    private lateinit var viewModel: MainViewModel
    private lateinit var imageView: ImageView
    private lateinit var prevButton: Button
    private lateinit var nextButton: Button

    private val scopeProvider by lazy { AndroidLifecycleScopeProvider.from(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageView = findViewById(R.id.imageview_main)
        prevButton = findViewById(R.id.button_prev)
        nextButton = findViewById(R.id.button_next)

        viewModel = ViewModelProviders
                .of(this, vmFactory)
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
                is MainActivityState.Error -> {
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
