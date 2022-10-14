package com.hirin.story.data.user

import com.haroldadmin.cnradapter.NetworkResponse
import com.hirin.story.BuildConfig
import com.hirin.story.data.BasicResponse
import com.hirin.story.data.GenericErrorResponse
import com.hirin.story.data.user.request.LoginRequest
import com.hirin.story.data.user.request.RegisterRequest
import com.hirin.story.data.user.response.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface UserService {
    @POST(BuildConfig.REGISTER_URL)
    suspend fun sendingRegister(
        @Body registerRequest: RegisterRequest
    ): NetworkResponse<BasicResponse, GenericErrorResponse>

    @POST(BuildConfig.LOGIN_URL)
    suspend fun sendingLogin(
        @Body loginRequest: LoginRequest
    ): NetworkResponse<LoginResponse, GenericErrorResponse>
}