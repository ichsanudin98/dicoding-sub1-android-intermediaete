package com.hirin.story.ui.main.pages.momentlist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingSource
import com.hirin.story.data.GenericErrorResponse
import com.hirin.story.data.moment.MomentPagingSource
import com.hirin.story.data.moment.MomentRepositoryImpl
import com.hirin.story.data.moment.MomentService
import com.hirin.story.data.moment.response.MomentListResponse
import com.hirin.story.domain.moment.MomentListWithPagingUseCase
import com.hirin.story.domain.moment.MomentRepository
import com.hirin.story.utils.DataDummy
import com.hirin.story.utils.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.KoinTest
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MomentListViewModelTest: KoinTest {
    private lateinit var viewModel: MomentListViewModel
    private lateinit var momentListUseCase: MomentListWithPagingUseCase
    private lateinit var repository: MomentRepository
    private lateinit var service: MomentService
    private lateinit var pagingSource: MomentPagingSource

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
        service = Mockito.mock(MomentService::class.java)
        momentListUseCase = MomentListWithPagingUseCase(repository)
        viewModel = MomentListViewModel(momentListUseCase)
        pagingSource = MomentPagingSource(service)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `when getting moment response success`() {
        runTest {
            Mockito.`when`(service.getMoment(1, 1, 0)).thenReturn(dummyMoment)

            val expectedResult = PagingSource.LoadResult.Page(
                data = dummyMoment.listStory,
                prevKey = null,
                nextKey = 2
            )
            val actualResult = pagingSource.load(
                PagingSource.LoadParams.Append(
                    key = 1,
                    loadSize = 1,
                    placeholdersEnabled = false
                )
            )

            Assert.assertEquals(
                expectedResult,
                actualResult
            )
        }
    }
}