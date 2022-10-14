package com.hirin.story.data.user.request

import com.google.gson.annotations.SerializedName

data class RegisterRequest (
    @SerializedName("name") private val name: String,
    @SerializedName("email") private val email: String,
    @SerializedName("password") private val password: String
)