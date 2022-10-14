package com.hirin.story.utils.extension

import androidx.lifecycle.LifecycleOwner
import com.hirin.story.utils.PostLiveData

fun <T> PostLiveData<T?>.observeNonNull(owner: LifecycleOwner, observer: (T) -> Unit) {
    this.observe(owner) {
        it?.let {
            observer(it)
        }
    }
}