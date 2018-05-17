package com.arjunalabs.android.xkcdreader

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider

class MainViewModelFactory(private val getComicByNumber: GetComicByNumber, private val getLatestComic: GetLatestComic) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(getComicByNumber, getLatestComic) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}