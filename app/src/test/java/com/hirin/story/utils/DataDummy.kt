package com.hirin.story.utils

import com.hirin.story.data.moment.response.MomentListResponse
import com.hirin.story.data.moment.response.MomentListStoryResponse

object DataDummy {
    fun generateDataDummyMomentList(): MomentListResponse {
        val momentList = mutableListOf<MomentListStoryResponse>()
        for (i in 0..10) {
            val moment = MomentListStoryResponse(
                "$i",
                "name $i",
                "description $i",
                "https://dicoding-web-img.sgp1.cdn.digitaloceanspaces.com/original/commons/feature-1-kurikulum-global-3.png",
                System.currentTimeMillis().toString(),
                -6.1018033,
                106.9240557
            )
            momentList.add(moment)
        }
        return MomentListResponse(momentList)
    }
}