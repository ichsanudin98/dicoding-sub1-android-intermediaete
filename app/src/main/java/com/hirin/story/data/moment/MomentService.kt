package com.hirin.story.data.moment

import com.haroldadmin.cnradapter.NetworkResponse
import com.hirin.story.BuildConfig
import com.hirin.story.data.BasicResponse
import com.hirin.story.data.GenericErrorResponse
import com.hirin.story.data.moment.response.MomentListResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface MomentService {
    @GET(BuildConfig.ALL_STORIES_URL)
    suspend fun getAllMoment(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("location") location: Int = 0,
    ): NetworkResponse<MomentListResponse, GenericErrorResponse>

    @Multipart
    @POST(BuildConfig.ADD_STORY_URL)
    suspend fun createMoment(
        @PartMap partMap: HashMap<String, RequestBody>,
        @Part file: MultipartBody.Part
    ): NetworkResponse<BasicResponse, GenericErrorResponse>
}