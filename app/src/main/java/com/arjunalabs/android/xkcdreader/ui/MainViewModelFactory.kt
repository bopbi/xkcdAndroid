package com.arjunalabs.android.xkcdreader.ui

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.arjunalabs.android.xkcdreader.usecase.GetComicByNumber
import com.arjunalabs.android.xkcdreader.usecase.GetLatestComic

class MainViewModelFactory(private val getComicByNumber: GetComicByNumber, private val getLatestComic: GetLatestComic) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(getComicByNumber, getLatestComic) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}