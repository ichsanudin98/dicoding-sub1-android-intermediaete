package com.hirin.story.data.moment

import android.content.Context
import com.haroldadmin.cnradapter.NetworkResponse
import com.hirin.story.data.BasicResponse
import com.hirin.story.data.GenericErrorResponse
import com.hirin.story.data.moment.response.MomentListResponse
import com.hirin.story.domain.moment.MomentRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class MomentRepositoryImpl(private val context: Context, private val momentService: MomentService): MomentRepository {
    override suspend fun getMoment(
        page: Int,
        size: Int,
        location: Int
    ): NetworkResponse<MomentListResponse, GenericErrorResponse> {
        return momentService.getAllMoment(page, size, location)
    }

    override suspend fun createMoment(
        photoFile: File,
        description: String,
        latitude: String?,
        longitude: String?
    ): NetworkResponse<BasicResponse, GenericErrorResponse> {
        val params = HashMap<String, RequestBody>()
        params["description"] = description.toRequestBody("text/plain".toMediaTypeOrNull())
        latitude?.let { params["lat"] = latitude.toRequestBody("text/plain".toMediaTypeOrNull()) }
        longitude?.let { params["lon"] = longitude.toRequestBody("text/plain".toMediaTypeOrNull()) }

        val file = MultipartBody.Part.createFormData(
            "photo",
            photoFile.name,
            photoFile.asRequestBody("image/*".toMediaTypeOrNull())
        )
        return momentService.createMoment(params, file)
    }
}