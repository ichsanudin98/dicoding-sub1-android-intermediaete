package com.hirin.story.data.user

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.haroldadmin.cnradapter.NetworkResponse
import com.hirin.story.data.BasicResponse
import com.hirin.story.data.GenericErrorResponse
import com.hirin.story.data.user.request.LoginRequest
import com.hirin.story.data.user.request.RegisterRequest
import com.hirin.story.data.user.response.LoginResponse
import com.hirin.story.domain.user.UserRepository
import com.hirin.story.utils.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.KoinTest
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
class UserRepositoryTest: KoinTest {
    private lateinit var service: UserService
    private lateinit var repository: UserRepository
    private lateinit var dummyError: GenericErrorResponse
    private lateinit var dummyData: BasicResponse
    private lateinit var dummyDataLogin: LoginResponse

    @Mock
    private var mockContext: Context = Mockito.mock(Context::class.java)

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        dummyError = GenericErrorResponse(
            messageRes = null,
            error = true,
            message = "Waduh error"
        )
        dummyData = BasicResponse().apply {
            message = "Yeay berhasil"
            error = false
        }
        dummyDataLogin = LoginResponse(
            result = LoginResponse.LoginResultResponse(
                userId = "1234",
                name = "namaku",
                token = "AizAHueHue"
            ),
        ).apply {
            message = "Yeay berhasil"
            error = false
        }
        service = mock(UserService::class.java)
        repository = UserRepositoryImpl(mockContext, service)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `when register get response success`() {
        runTest {
            val name = "namaku"
            val email = "wkwkwkwk@gmail.com"
            val password = "12345"
            val response = NetworkResponse.Success<BasicResponse, GenericErrorResponse>(body = dummyData, response = Response.success(200))
            Mockito.`when`(service.sendingRegister(RegisterRequest(name, email, password))).thenReturn(response)
            val result = repository.register(name, email, password)

            Assert.assertNotNull(result)
            Assert.assertTrue(result is NetworkResponse.Success)
            Assert.assertEquals(response, result)
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `when register get response failed`() {
        runTest {
            val name = "namaku"
            val email = "wkwkwkwk@gmail.com"
            val password = "12345"
            val response = NetworkResponse.ServerError<BasicResponse, GenericErrorResponse>(body = dummyError, response = Response.error<GenericErrorResponse>(400, dummyError.toString().toResponseBody("application/json".toMediaTypeOrNull())))
            Mockito.`when`(service.sendingRegister(RegisterRequest(name, email, password))).thenReturn(response)
            val result = repository.register(name, email, password)

            Assert.assertNotNull(result)
            Assert.assertTrue(result is NetworkResponse.Error)
            Assert.assertEquals(response, result)
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `when login get response success`() {
        runTest {
            val email = "wkwkwkwk@gmail.com"
            val password = "12345"
            val response = NetworkResponse.Success<LoginResponse, GenericErrorResponse>(body = dummyDataLogin, response = Response.success(200))
            Mockito.`when`(service.sendingLogin(LoginRequest(email, password))).thenReturn(response)
            val result = repository.login(email, password)

            Assert.assertNotNull(result)
            Assert.assertTrue(result is NetworkResponse.Success)
            Assert.assertEquals(response, result)
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `when login get response failed`() {
        runTest {
            val email = "wkwkwkwk@gmail.com"
            val password = "12345"
            val response = NetworkResponse.ServerError<LoginResponse, GenericErrorResponse>(body = dummyError, response = Response.error<GenericErrorResponse>(400, dummyError.toString().toResponseBody("application/json".toMediaTypeOrNull())))
            Mockito.`when`(service.sendingLogin(LoginRequest(email, password))).thenReturn(response)
            val result = repository.login(email, password)

            Assert.assertNotNull(result)
            Assert.assertTrue(result is NetworkResponse.Error)
            Assert.assertEquals(response, result)
        }
    }
}