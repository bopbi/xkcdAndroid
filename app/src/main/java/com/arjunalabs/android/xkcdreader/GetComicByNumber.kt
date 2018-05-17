package com.arjunalabs.android.xkcdreader

class GetComicByNumber(private val xkcdService: XKCDService) {

    fun execute(numberString: String) = xkcdService.getComicByNumber(numberString)
}