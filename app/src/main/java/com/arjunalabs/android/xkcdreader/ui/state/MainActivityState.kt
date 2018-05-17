package com.arjunalabs.android.xkcdreader.ui.state

import com.arjunalabs.android.xkcdreader.repository.XkcdData

data class MainActivityState(val isInitialized: Boolean = false, val data: XkcdData? = null)