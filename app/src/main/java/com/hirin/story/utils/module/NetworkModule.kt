package com.hirin.story.utils.module

import com.hirin.story.BuildConfig
import com.hirin.story.data.moment.MomentService
import com.hirin.story.data.user.UserService
import com.hirin.story.utils.NetworkUtil
import org.koin.dsl.module

val NetworkModule = module {
    /** REST API */
    single { NetworkUtil.buildClient(get()) }
    single { NetworkUtil.buildService<UserService>(BuildConfig.BASE_URL, get()) }
    single { NetworkUtil.buildService<MomentService>(BuildConfig.BASE_URL, get()) }
}