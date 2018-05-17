package com.arjunalabs.android.xkcdreader.usecase

import com.arjunalabs.android.xkcdreader.repository.XKCDService

class GetLatestComicImpl(private val xkcdService: XKCDService) : GetLatestComic {

    override fun execute() = xkcdService.getLatestComic()
}