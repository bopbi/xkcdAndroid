package com.arjunalabs.android.xkcdreader.dagger

import com.arjunalabs.android.xkcdreader.ui.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [MyApplicationModule::class])

interface MyApplicationComponent {

    fun inject(mainActivity: MainActivity)
}
