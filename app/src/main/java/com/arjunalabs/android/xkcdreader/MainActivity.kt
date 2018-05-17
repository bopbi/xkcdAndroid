package com.arjunalabs.android.xkcdreader

import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val xkcdService = XKCDService.create()
        val getLatestComic = GetLatestComic(xkcdService)
        val getComicByNumber = GetComicByNumber(xkcdService)
        viewModel = ViewModelProviders.of(this, MainViewModelFactory(getComicByNumber, getLatestComic)).get(MainViewModel::class.java)
        viewModel.getState()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    Log.v(">XKCD APP", it.toString())
                    title = it.data?.title ?: ""
                }

    }

    override fun onResume() {
        super.onResume()
        viewModel.loadIfNotInitialized()
    }
}
