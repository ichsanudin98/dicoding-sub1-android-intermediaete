package com.hirin.story.ui.main.pages.momentcreate

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.haroldadmin.cnradapter.NetworkResponse
import com.hirin.story.data.BasicResponse
import com.hirin.story.data.GenericErrorResponse
import com.hirin.story.data.moment.MomentRepositoryImpl
import com.hirin.story.domain.moment.MomentCreateUseCase
import com.hirin.story.domain.moment.MomentRepository
import com.hirin.story.utils.ImageUtil
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
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response
import java.io.File

@RunWith(MockitoJUnitRunner::class)
class MomentCreateViewModelTest: KoinTest {
    private lateinit var viewModel: MomentCreateViewModel
    private lateinit var momentCreateUseCase: MomentCreateUseCase
    private lateinit var repository: MomentRepository
    private lateinit var dummyError: GenericErrorResponse
    private lateinit var dummyData: BasicResponse

    @Mock
    private var mockImageUtil: ImageUtil = mock(ImageUtil::class.java)

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
        repository = Mockito.mock(MomentRepositoryImpl::class.java)
        momentCreateUseCase = MomentCreateUseCase(repository)
        viewModel = MomentCreateViewModel(momentCreateUseCase)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `when register get response success`() {
        runTest {
            val description = "deskripsi"
            val file = File("app/src/main/res/drawable/ig_flag_indonesian.png")
            val response = NetworkResponse.Success<BasicResponse, GenericErrorResponse>(body = dummyData, response = Response.success(200))
            Mockito.`when`(repository.createMoment(
                file, description, null, null)).thenReturn(response)

            viewModel.createMoment(file, description, null, null)

            val loadingState = viewModel.loadingWidgetLiveData.getOrAwaitValue()
            val createMomentState = viewModel.momentCreateLiveData.getOrAwaitValue()

            Mockito.verify(repository).createMoment(file, description, null, null)

            Assert.assertNotNull(loadingState)

            Assert.assertNotNull(createMomentState)
            Assert.assertTrue(createMomentState is BasicResponse)
            Assert.assertEquals(response.body, createMomentState)
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `when register get response failed`() {
        runTest {
            val description = "deskripsi"
            val file = File("app/src/main/res/drawable/ig_flag_indonesian.png")
            val response = NetworkResponse.ServerError<BasicResponse, GenericErrorResponse>(body = dummyError, response = Response.error<GenericErrorResponse>(400, dummyError.toString().toResponseBody("application/json".toMediaTypeOrNull())))
            Mockito.`when`(repository.createMoment(
                file, description, null, null)).thenReturn(response)

            viewModel.createMoment(file, description, null, null)

            val loadingState = viewModel.loadingWidgetLiveData.getOrAwaitValue()
            val errorState = viewModel.genericErrorLiveData.getOrAwaitValue()

            Mockito.verify(repository).createMoment(file, description, null, null)

            Assert.assertNotNull(loadingState)

            Assert.assertNotNull(errorState)
            Assert.assertTrue(errorState is GenericErrorResponse)
            Assert.assertEquals(response.body, errorState)
        }
    }
}