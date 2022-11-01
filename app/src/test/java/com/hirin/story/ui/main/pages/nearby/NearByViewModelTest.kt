package com.hirin.story.ui.main.pages.nearby

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.haroldadmin.cnradapter.NetworkResponse
import com.hirin.story.data.GenericErrorResponse
import com.hirin.story.data.moment.MomentRepositoryImpl
import com.hirin.story.data.moment.response.MomentListResponse
import com.hirin.story.domain.moment.MomentListUseCase
import com.hirin.story.domain.moment.MomentRepository
import com.hirin.story.utils.DataDummy
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
class NearByViewModelTest: KoinTest {
    private lateinit var viewModel: NearByViewModel
    private lateinit var momentListUseCase: MomentListUseCase
    private lateinit var repository: MomentRepository

    private lateinit var dummyError: GenericErrorResponse
    private lateinit var dummyMoment: MomentListResponse

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
        dummyMoment = DataDummy.generateDataDummyMomentList()

        repository = Mockito.mock(MomentRepositoryImpl::class.java)
        momentListUseCase = MomentListUseCase(repository)
        viewModel = NearByViewModel(momentListUseCase)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `when getting moment using location response success`() {
        runTest {
            val response = NetworkResponse.Success<MomentListResponse, GenericErrorResponse>(body = dummyMoment, response = Response.success(200))
            Mockito.`when`(repository.getMoment(1, 1, 1)).thenReturn(response)

            viewModel.getAllMoment(1, 1, 1)

            val loadingState = viewModel.loadingWidgetLiveData.getOrAwaitValue()
            val nearByState = viewModel.momentListLiveData.getOrAwaitValue()

            Mockito.verify(repository).getMoment(1, 1, 1)

            Assert.assertNotNull(loadingState)

            Assert.assertNotNull(nearByState)
            Assert.assertTrue(nearByState is MomentListResponse)
            Assert.assertEquals(response.body, nearByState)
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `when getting moment using location response failed`() {
        runTest {
            val response = NetworkResponse.ServerError<MomentListResponse, GenericErrorResponse>(body = dummyError, response = Response.error<GenericErrorResponse>(400, dummyError.toString().toResponseBody("application/json".toMediaTypeOrNull())))
            Mockito.`when`(repository.getMoment(1, 1, 1)).thenReturn(response)

            viewModel.getAllMoment(1, 1, 1)

            val loadingState = viewModel.loadingWidgetLiveData.getOrAwaitValue()
            val errorState = viewModel.genericErrorLiveData.getOrAwaitValue()

            Mockito.verify(repository).getMoment(1,1, 1)

            Assert.assertNotNull(loadingState)

            Assert.assertNotNull(errorState)
            Assert.assertTrue(errorState is GenericErrorResponse)
            Assert.assertEquals(response.body, errorState)
        }
    }
}