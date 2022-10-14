package com.hirin.story.domain.user

import com.haroldadmin.cnradapter.NetworkResponse
import com.hirin.story.data.BasicResponse
import com.hirin.story.data.GenericErrorResponse
import com.hirin.story.data.user.response.LoginResponse

interface UserRepository {
    suspend fun register(
        name: String,
        email: String,
        password: String
    ): NetworkResponse<BasicResponse, GenericErrorResponse>

    suspend fun login(
        email: String,
        password: String
    ): NetworkResponse<LoginResponse, GenericErrorResponse>
}