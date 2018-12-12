package com.arjunalabs.android.xkcdreader

import android.app.Activity
import android.app.Application
import com.arjunalabs.android.xkcdreader.dagger.DaggerMyApplicationComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject


class MyApplication : Application(), HasActivityInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun activityInjector(): AndroidInjector<Activity> = dispatchingAndroidInjector

    override fun onCreate() {
        super.onCreate()

        DaggerMyApplicationComponent
                .builder()
                .appContext(this)
                .create(this)
                .inject(this)
    }
}
