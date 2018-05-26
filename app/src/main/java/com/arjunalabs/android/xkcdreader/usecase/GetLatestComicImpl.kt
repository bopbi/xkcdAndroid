package com.arjunalabs.android.xkcdreader.usecase

import com.arjunalabs.android.xkcdreader.repository.XKCDService
import com.arjunalabs.android.xkcdreader.usecase.GetLatestComicResult.*
import io.reactivex.Observable

class GetLatestComicImpl(private val xkcdService: XKCDService) : GetLatestComic {

    override fun execute(): Observable<GetLatestComicResult> = xkcdService.getLatestComic()
            .map<GetLatestComicResult>{ Success(it) }
            .startWith(Loading) // if this change to lambda the emit not work https://stackoverflow.com/questions/49602609/nothing-executes-after-startwith-operator-in-rxjava
            .onErrorResumeNext(Observable.just(Error("")))

}