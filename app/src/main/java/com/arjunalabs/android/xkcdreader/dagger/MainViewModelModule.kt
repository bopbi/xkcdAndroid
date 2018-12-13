package com.arjunalabs.android.xkcdreader.dagger

import com.arjunalabs.android.xkcdreader.repository.XKCDService
import com.arjunalabs.android.xkcdreader.usecase.GetComicByNumber
import com.arjunalabs.android.xkcdreader.usecase.GetComicByNumberImpl
import com.arjunalabs.android.xkcdreader.usecase.GetLatestComic
import com.arjunalabs.android.xkcdreader.usecase.GetLatestComicImpl
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Named

@Module(
        includes = [RetrofitModule::class]
)
class MainViewModelModule {

    @Provides
    fun providesGetComicByNumber(xkcdService: XKCDService): GetComicByNumber {
        return GetComicByNumberImpl(xkcdService)
    }

    @Provides
    fun providesGetLatestComic(xkcdService: XKCDService): GetLatestComic {
        return GetLatestComicImpl(xkcdService)
    }

    @Provides
    @Named("schedulersIo")
    fun providesSchedulersIo(): Scheduler = Schedulers.io()

    @Provides
    @Named("schedulersMainThread")
    fun providesSchedulersMainThread(): Scheduler = AndroidSchedulers.mainThread()
}
