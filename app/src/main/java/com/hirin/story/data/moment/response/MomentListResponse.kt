package com.hirin.story.data.moment.response

import com.google.gson.annotations.SerializedName
import com.hirin.story.data.BasicResponse

class MomentListResponse(
    @SerializedName("listStory") var listStory: MutableList<MomentListStoryResponse> = mutableListOf()
): BasicResponse()