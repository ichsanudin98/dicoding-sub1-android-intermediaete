package com.hirin.story

import android.app.Application
import com.hirin.story.utils.SharedPreferencesUtil
import com.hirin.story.utils.module.DomainModule
import com.hirin.story.utils.module.NetworkModule
import com.hirin.story.utils.module.RepositoryModule
import com.hirin.story.utils.module.ViewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.EmptyLogger

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        SharedPreferencesUtil.init(this)
        startKoin {
            if (BuildConfig.DEBUG) androidLogger() else EmptyLogger()
            androidContext(applicationContext)
            modules(
                listOf(
                    ViewModelModule,
                    DomainModule,
                    NetworkModule,
                    RepositoryModule,
                )
            )
        }
    }
}