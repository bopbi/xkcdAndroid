package com.arjunalabs.android.xkcdreader.usecase

import com.arjunalabs.android.xkcdreader.repository.XKCDService
import com.arjunalabs.android.xkcdreader.usecase.GetComicResult.*
import io.reactivex.Observable

class GetLatestComicImpl(private val xkcdService: XKCDService) : GetLatestComic {

    override fun execute(): Observable<GetComicResult> = xkcdService.getLatestComic()
            .map<GetComicResult>{ Success(it) }
            .startWith(Loading) // if this change to lambda the emit not work https://stackoverflow.com/questions/49602609/nothing-executes-after-startwith-operator-in-rxjava
            .onErrorReturn { Error("")}

}