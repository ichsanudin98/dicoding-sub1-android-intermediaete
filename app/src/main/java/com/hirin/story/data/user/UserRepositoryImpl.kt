package com.hirin.story.data.user

import android.content.Context
import com.haroldadmin.cnradapter.NetworkResponse
import com.hirin.story.data.BasicResponse
import com.hirin.story.data.GenericErrorResponse
import com.hirin.story.data.user.request.LoginRequest
import com.hirin.story.data.user.request.RegisterRequest
import com.hirin.story.data.user.response.LoginResponse
import com.hirin.story.domain.user.UserRepository

class UserRepositoryImpl(private val context: Context, private val userService: UserService): UserRepository {
    override suspend fun register(
        name: String,
        email: String,
        password: String
    ): NetworkResponse<BasicResponse, GenericErrorResponse> {
        return userService.sendingRegister(
            RegisterRequest(name, email, password)
        )
    }

    override suspend fun login(
        email: String,
        password: String
    ): NetworkResponse<LoginResponse, GenericErrorResponse> {
        return userService.sendingLogin(
            LoginRequest(email, password)
        )
    }
}