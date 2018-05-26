package com.arjunalabs.android.xkcdreader.usecase

import com.arjunalabs.android.xkcdreader.repository.XkcdData

sealed class GetLatestComicResult {
    object Loading : GetLatestComicResult()
    data class Error(val errorString: String) : GetLatestComicResult()
    data class Success(val data: XkcdData) : GetLatestComicResult()
}