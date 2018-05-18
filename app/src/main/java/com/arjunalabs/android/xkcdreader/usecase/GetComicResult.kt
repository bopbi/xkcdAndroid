package com.arjunalabs.android.xkcdreader.usecase

import com.arjunalabs.android.xkcdreader.repository.XkcdData

sealed class GetComicResult {
    object Loading : GetComicResult()
    data class Error(val errorString: String) : GetComicResult()
    data class Success(val data: XkcdData) : GetComicResult()
}