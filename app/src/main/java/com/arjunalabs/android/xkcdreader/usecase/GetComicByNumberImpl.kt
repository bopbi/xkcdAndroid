package com.arjunalabs.android.xkcdreader.usecase

import com.arjunalabs.android.xkcdreader.repository.XKCDService

class GetComicByNumberImpl(private val xkcdService: XKCDService) : GetComicByNumber {

    override fun execute(numberString: String) = xkcdService.getComicByNumber(numberString)
}