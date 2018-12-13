package com.arjunalabs.android.xkcdreader.dagger

import android.content.Context
import com.arjunalabs.android.xkcdreader.MyApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidInjectionModule::class,
    MyApplicationModule::class,
    MainViewModelModule::class
])

interface MyApplicationComponent : AndroidInjector<MyApplication> {

    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<MyApplication>() {
        @BindsInstance
        abstract fun appContext(appContext: Context): Builder
    }
}
