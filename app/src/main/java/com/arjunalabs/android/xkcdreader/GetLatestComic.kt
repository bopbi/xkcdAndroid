package com.arjunalabs.android.xkcdreader

class GetLatestComic(private val xkcdService: XKCDService) {

    fun execute() = xkcdService.getLatestComic()
}