package com.arjunalabs.android.xkcdreader.ui.state

import com.arjunalabs.android.xkcdreader.repository.XkcdData

data class MainActivityState(
        var loading: Boolean,
        var error: Boolean,
        var uninitialized: Boolean,
        var data: XkcdData?,
        var prevButtonEnabled: Boolean = false,
        var nextButtonEnabled: Boolean = false
)

