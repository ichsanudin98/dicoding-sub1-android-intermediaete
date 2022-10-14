package com.hirin.story.utils.module

import com.hirin.story.ui.auth.AuthViewModel
import com.hirin.story.ui.auth.pages.login.LoginViewModel
import com.hirin.story.ui.auth.pages.register.RegisterViewModel
import com.hirin.story.ui.localization.LocalizationViewModel
import com.hirin.story.ui.main.MainViewModel
import com.hirin.story.ui.main.pages.momentcreate.MomentCreateViewModel
import com.hirin.story.ui.main.pages.momentlist.MomentListViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val ViewModelModule = module {
    viewModel { AuthViewModel() }
    viewModel { MainViewModel() }
    viewModel { LocalizationViewModel() }

    viewModel { RegisterViewModel(get()) }
    viewModel { LoginViewModel(get()) }

    viewModel { MomentListViewModel(get()) }
    viewModel { MomentCreateViewModel(androidContext(), get()) }
}