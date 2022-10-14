package com.hirin.story.domain.moment

import com.haroldadmin.cnradapter.NetworkResponse
import com.hirin.story.data.BasicResponse
import com.hirin.story.data.GenericErrorResponse
import com.hirin.story.data.moment.response.MomentListResponse
import java.io.File

interface MomentRepository {
    suspend fun getMoment(
        page: Int,
        size: Int,
        location: Int
    ): NetworkResponse<MomentListResponse, GenericErrorResponse>

    suspend fun createMoment(
        photoFile: File,
        description: String,
        latitude: String?,
        longitude: String?
    ): NetworkResponse<BasicResponse, GenericErrorResponse>
}