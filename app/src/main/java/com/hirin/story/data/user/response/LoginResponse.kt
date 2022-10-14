package com.hirin.story.data.user.response

import com.google.gson.annotations.SerializedName
import com.hirin.story.data.BasicResponse

class LoginResponse(
    @SerializedName("loginResult") var result: LoginResultResponse?
): BasicResponse() {
    data class LoginResultResponse(
        @SerializedName("userId") var userId: String,
        @SerializedName("name") var name: String,
        @SerializedName("token") var token: String
    )
}