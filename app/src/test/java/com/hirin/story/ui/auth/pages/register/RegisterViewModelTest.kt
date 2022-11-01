package com.hirin.story.ui.auth.pages.register

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.haroldadmin.cnradapter.NetworkResponse
import com.hirin.story.data.BasicResponse
import com.hirin.story.data.GenericErrorResponse
import com.hirin.story.data.user.UserRepositoryImpl
import com.hirin.story.domain.user.RegisterUseCase
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
class RegisterViewModelTest: KoinTest {
    private lateinit var viewModel: RegisterViewModel
    private lateinit var registerUseCase: RegisterUseCase
    private lateinit var repository: UserRepository
    private lateinit var dummyError: GenericErrorResponse
    private lateinit var dummyData: BasicResponse

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
        repository = Mockito.mock(UserRepositoryImpl::class.java)
        registerUseCase = RegisterUseCase(repository)
        viewModel = RegisterViewModel(registerUseCase)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `when register get response success`() {
        runTest {
            val name = "namaku"
            val email = "wkwkwkwk@gmail.com"
            val password = "12345"
            val response = NetworkResponse.Success<BasicResponse, GenericErrorResponse>(body = dummyData, response = Response.success(200))
            Mockito.`when`(repository.register(name, email, password)).thenReturn(response)

            viewModel.register(name, email, password)

            val loadingState = viewModel.loadingWidgetLiveData.getOrAwaitValue()
            val registerState = viewModel.registerLiveData.getOrAwaitValue()

            Mockito.verify(repository).register(name, email, password)

            Assert.assertNotNull(loadingState)

            Assert.assertNotNull(registerState)
            Assert.assertTrue(registerState is BasicResponse)
            Assert.assertEquals(response.body, registerState)
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
            Mockito.`when`(repository.register(name, email, password)).thenReturn(response)

            viewModel.register(name, email, password)

            val loadingState = viewModel.loadingWidgetLiveData.getOrAwaitValue()
            val errorState = viewModel.genericErrorLiveData.getOrAwaitValue()

            Mockito.verify(repository).register(name, email, password)

            Assert.assertNotNull(loadingState)

            Assert.assertNotNull(errorState)
            Assert.assertTrue(errorState is GenericErrorResponse)
            Assert.assertEquals(response.body, errorState)
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `when confirmation password unmatch with password`() {
        runTest {
            val password = "123456"
            val confirmPassword = "1234567"
            viewModel.isValidatePasswordConfirm(password, confirmPassword)

            val passwordConfirmationState = viewModel.passwordConfirmValidationLiveData.getOrAwaitValue()

            Assert.assertNotNull(passwordConfirmationState)
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `when confirmation password match with password`() {
        runTest {
            val password = "123456"
            val confirmPassword = "123456"
            viewModel.isValidatePasswordConfirm(password, confirmPassword)

            val passwordConfirmationState = viewModel.passwordConfirmValidationLiveData.getOrAwaitValue()

            Assert.assertNull(passwordConfirmationState)
        }
    }
}