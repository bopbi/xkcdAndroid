package com.arjunalabs.android.xkcdreader.dagger

import com.arjunalabs.android.xkcdreader.ui.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MyApplicationModule {

    @ContributesAndroidInjector
    abstract fun contributeMainActivity(): MainActivity
}
