package com.hirin.story.data

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.hirin.story.R

@Keep
data class GenericErrorResponse (
    @SerializedName("message") var message: String,
    @SerializedName("messageRes") var messageRes: Int?,
    @SerializedName("error") var error: Boolean
) {
    companion object {
        fun generalError() = GenericErrorResponse(
            error = true,
            message = "",
            messageRes = R.string.general_error
        )
    }
}