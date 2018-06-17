package com.arjunalabs.android.xkcdreader.ui.state

import com.arjunalabs.android.xkcdreader.repository.XkcdData

data class MainActivityState(
        val loading: Boolean,
        val error: Boolean,
        val uninitialized: Boolean,
        val data: XkcdData?,
        val prevButtonEnabled: Boolean = false,
        val nextButtonEnabled: Boolean = false,
        val currentNum: Int,
        val latestNum: Int
)
