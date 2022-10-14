package com.hirin.story.data.moment.request

import com.google.gson.annotations.SerializedName

data class MomentListRequest (
    @SerializedName("page") private val page: Int,
    @SerializedName("size") private val size: Int,
    @SerializedName("location") private val location: Byte
)