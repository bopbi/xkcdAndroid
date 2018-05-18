package com.arjunalabs.android.xkcdreader.ui.state

import com.arjunalabs.android.xkcdreader.repository.XkcdData

sealed class MainActivityState {
    object Uninitialized : MainActivityState()
    object Loading : MainActivityState()
    data class Error(val errorString: String) : MainActivityState()
    data class Data(val data: XkcdData, var prevButtonEnabled: Boolean = false, var nextButtonEnabled: Boolean = false) : MainActivityState()
}

