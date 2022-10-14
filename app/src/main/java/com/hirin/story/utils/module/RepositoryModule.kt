package com.hirin.story.utils.module

import com.hirin.story.data.moment.MomentRepositoryImpl
import com.hirin.story.data.user.UserRepositoryImpl
import com.hirin.story.domain.moment.MomentRepository
import com.hirin.story.domain.user.UserRepository
import org.koin.dsl.module

val RepositoryModule = module {
    /** Dicoding API Repository */
    single<UserRepository> { UserRepositoryImpl(get(), get()) }
    single<MomentRepository> { MomentRepositoryImpl(get(), get()) }
}