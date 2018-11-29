package com.arjunalabs.android.xkcdreader.dagger

import com.arjunalabs.android.xkcdreader.repository.XKCDService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class MyApplicationModule {

    @Provides
    @Singleton
    fun provideXKCDService(): XKCDService {
        return XKCDService.create()
    }
}
