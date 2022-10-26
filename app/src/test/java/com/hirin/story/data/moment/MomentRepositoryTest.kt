package com.hirin.story.data.moment

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.haroldadmin.cnradapter.NetworkResponse
import com.hirin.story.data.GenericErrorResponse
import com.hirin.story.data.moment.response.MomentListResponse
import com.hirin.story.domain.moment.MomentRepository
import com.hirin.story.utils.DataDummy
import com.hirin.story.utils.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.koin.test.KoinTest
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response
import kotlin.test.Test

@RunWith(MockitoJUnitRunner::class)
class MomentRepositoryTest: KoinTest {
    private lateinit var service: MomentService
    private lateinit var repository: MomentRepository
    private lateinit var dummyError: GenericErrorResponse

    @Mock
    private var mockContext: Context = mock(Context::class.java)

    @Mock
    private val dummyMoment = DataDummy.generateDataDummyMomentList()

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
        service = mock(MomentService::class.java)
        repository = MomentRepositoryImpl(mockContext, service)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `when get response success`() {
        runTest {
            val response = NetworkResponse.Success<MomentListResponse, GenericErrorResponse>(body = dummyMoment, response = Response.success(200))
            `when`(service.getAllMoment(1, 1, 1)).thenReturn(response)
            val result = repository.getMoment(1, 1, 1)

            Assert.assertNotNull(result)
            Assert.assertTrue(result is NetworkResponse.Success)
            assertEquals(response, result)
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `when get response failed`() {
        runTest {
            val response = NetworkResponse.ServerError<MomentListResponse, GenericErrorResponse>(body = dummyError, response = Response.error<GenericErrorResponse>(400, dummyError.toString().toResponseBody("application/json".toMediaTypeOrNull())))
            `when`(service.getAllMoment(1, 1, 1)).thenReturn(response)
            val result = repository.getMoment(1, 1, 1)

            Assert.assertNotNull(result)
            Assert.assertTrue(result is NetworkResponse.Error)
            assertEquals(response, result)
        }
    }
}