package com.arjunalabs.android.xkcdreader.usecase

import com.arjunalabs.android.xkcdreader.repository.XKCDService

class GetLatestComic(private val xkcdService: XKCDService) {

    fun execute() = xkcdService.getLatestComic()
}