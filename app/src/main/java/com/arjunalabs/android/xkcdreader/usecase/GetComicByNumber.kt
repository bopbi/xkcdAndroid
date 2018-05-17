package com.arjunalabs.android.xkcdreader.usecase

import com.arjunalabs.android.xkcdreader.repository.XKCDService

class GetComicByNumber(private val xkcdService: XKCDService) {

    fun execute(numberString: String) = xkcdService.getComicByNumber(numberString)
}