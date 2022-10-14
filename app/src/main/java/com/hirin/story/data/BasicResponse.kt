package com.hirin.story.data

import com.google.gson.annotations.SerializedName

open class BasicResponse {
    @SerializedName("message")
    var message: String = ""
    @SerializedName("error")
    var error: Boolean = false
}