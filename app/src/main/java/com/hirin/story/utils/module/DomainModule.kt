package com.hirin.story.utils.module

import com.hirin.story.domain.moment.MomentCreateUseCase
import com.hirin.story.domain.moment.MomentListUseCase
import com.hirin.story.domain.user.LoginUseCase
import com.hirin.story.domain.user.RegisterUseCase
import org.koin.dsl.module

val DomainModule = module {
    /** User UseCase */
    single { RegisterUseCase(get()) }
    single { LoginUseCase(get()) }

    /** Moment UseCase */
    single { MomentListUseCase(get()) }
    single { MomentCreateUseCase(get()) }
}