package com.hirin.story.ui.auth

import androidx.lifecycle.viewModelScope
import com.hirin.story.ui.base.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AuthViewModel: BaseViewModel() {
    var doubleBackToExitPressedOnce = false

    fun checkDoubleBackFlag(onTrue: () -> Unit, onFalse: (delay:Long) -> Unit) =
        when (doubleBackToExitPressedOnce) {
            true -> onTrue()
            else -> {
                doubleBackToExitPressedOnce = true
                val delay = 2000L
                onFalse(delay)
                resetDoubleBackFlag(delay)
            }
        }

    fun resetDoubleBackFlag(delay:Long){
        viewModelScope.launch {
            delay(delay)
            doubleBackToExitPressedOnce = false
        }
    }
}