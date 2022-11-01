package com.hirin.story.ui.auth.pages.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.haroldadmin.cnradapter.NetworkResponse
import com.hirin.story.data.BasicResponse
import com.hirin.story.data.GenericErrorResponse
import com.hirin.story.data.user.UserRepositoryImpl
import com.hirin.story.data.user.response.LoginResponse
import com.hirin.story.domain.user.LoginUseCase
import com.hirin.story.domain.user.UserRepository
import com.hirin.story.utils.MainDispatcherRule
import com.hirin.story.utils.getOrAwaitValue
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
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest: KoinTest {
    private lateinit var viewModel: LoginViewModel
    private lateinit var loginUseCase: LoginUseCase
    private lateinit var repository: UserRepository
    private lateinit var dummyError: GenericErrorResponse
    private lateinit var dummyData: BasicResponse
    private lateinit var dummyDataLogin: LoginResponse

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
        repository = Mockito.mock(UserRepositoryImpl::class.java)
        loginUseCase = LoginUseCase(repository)
        viewModel = LoginViewModel(loginUseCase)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `when login get response success`() {
        runTest {
            val email = "wkwkwkwk@gmail.com"
            val password = "12345"
            val response = NetworkResponse.Success<LoginResponse, GenericErrorResponse>(body = dummyDataLogin, response = Response.success(200))
            Mockito.`when`(repository.login(email, password)).thenReturn(response)

            viewModel.login(email, password)

            val loadingState = viewModel.loadingWidgetLiveData.getOrAwaitValue()
            val loginState = viewModel.loginLiveData.getOrAwaitValue()

            Mockito.verify(repository).login(email, password)

            Assert.assertNotNull(loadingState)

            Assert.assertNotNull(loginState)
            Assert.assertTrue(loginState is LoginResponse)
            Assert.assertEquals(response.body, loginState)
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `when login get response failed`() {
        runTest {
            val email = "wkwkwkwk@gmail.com"
            val password = "12345"
            val response = NetworkResponse.ServerError<LoginResponse, GenericErrorResponse>(body = dummyError, response = Response.error<GenericErrorResponse>(400, dummyError.toString().toResponseBody("application/json".toMediaTypeOrNull())))
            Mockito.`when`(repository.login(email, password)).thenReturn(response)

            viewModel.login(email, password)

            val loadingState = viewModel.loadingWidgetLiveData.getOrAwaitValue()
            val errorState = viewModel.genericErrorLiveData.getOrAwaitValue()

            Mockito.verify(repository).login(email, password)

            Assert.assertNotNull(loadingState)

            Assert.assertNotNull(errorState)
            Assert.assertTrue(errorState is GenericErrorResponse)
            Assert.assertEquals(response.body, errorState)
        }
    }
}