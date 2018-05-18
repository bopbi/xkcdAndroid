package com.arjunalabs.android.xkcdreader.usecase

import com.arjunalabs.android.xkcdreader.repository.XKCDService
import com.arjunalabs.android.xkcdreader.usecase.GetComicResult.*
import io.reactivex.Observable

class GetComicByNumberImpl(private val xkcdService: XKCDService) : GetComicByNumber {

    override fun execute(numberString: String): Observable<GetComicResult> = xkcdService.getComicByNumber(numberString)
            .map<GetComicResult>{ Success(it) }
            .startWith(Loading) // if this change to lambda the emit not work https://stackoverflow.com/questions/49602609/nothing-executes-after-startwith-operator-in-rxjava
            .onErrorReturn { Error("") }
}