package com.arjunalabs.android.xkcdreader.dagger

import com.arjunalabs.android.xkcdreader.repository.XKCDService
import com.arjunalabs.android.xkcdreader.repository.XKCDServiceImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
open class MyApplicationModule {

    @Provides
    @Singleton
    open fun provideXKCDService(): XKCDService {
        return XKCDServiceImpl.create()
    }
}
