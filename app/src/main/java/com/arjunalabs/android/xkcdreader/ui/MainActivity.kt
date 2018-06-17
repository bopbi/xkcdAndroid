package com.arjunalabs.android.xkcdreader.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.arjunalabs.android.xkcdreader.R
import com.arjunalabs.android.xkcdreader.databinding.ActivityMainBinding
import com.arjunalabs.android.xkcdreader.repository.XKCDService
import com.arjunalabs.android.xkcdreader.usecase.GetComicByNumberImpl
import com.arjunalabs.android.xkcdreader.usecase.GetLatestComicImpl
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var imageView: ImageView
    private lateinit var prevButton: Button
    private lateinit var nextButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val xkcdService = XKCDService.create()
        val getLatestComic = GetLatestComicImpl(xkcdService)
        val getComicByNumber = GetComicByNumberImpl(xkcdService)

        viewModel = ViewModelProviders
                .of(this, MainViewModelFactory(getComicByNumber, getLatestComic))
                .get(MainViewModel::class.java)

        val binding = ActivityMainBinding.inflate(layoutInflater)

        binding?.let {
            it.viewmodel = viewModel
            it.setLifecycleOwner(this)
            setContentView(it.root)
        }

        imageView = findViewById(R.id.imageview_main)
        prevButton = findViewById(R.id.button_prev)
        nextButton = findViewById(R.id.button_next)

        prevButton.setOnClickListener {
            viewModel.prevComic()
        }

        nextButton.setOnClickListener {
            viewModel.nextComic()
        }

        viewModel.appState.observe(this, Observer {
            it?.let {
                it.data?.let {
                    Picasso.get().load(it.img).into(imageView)
                }

                if (it.loading) {
                    Toast.makeText(this, "Loading", Toast.LENGTH_SHORT).show()
                }

                if (it.uninitialized) {
                    viewModel.loadLatest()
                }
            }
        })
    }
}
