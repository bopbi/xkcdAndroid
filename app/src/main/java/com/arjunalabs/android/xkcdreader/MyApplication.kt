package com.arjunalabs.android.xkcdreader

import android.app.Application
import com.arjunalabs.android.xkcdreader.dagger.DaggerMyApplicationComponent
import com.arjunalabs.android.xkcdreader.dagger.MyApplicationComponent
import com.arjunalabs.android.xkcdreader.dagger.MyApplicationModule


class MyApplication : Application() {

    lateinit var myApplicationComponent: MyApplicationComponent

    override fun onCreate() {
        super.onCreate()
        this.myApplicationComponent = createMyComponent()
    }

    private fun createMyComponent() =
            DaggerMyApplicationComponent
                    .builder()
                    .myApplicationModule(MyApplicationModule())
                    .build()

}
