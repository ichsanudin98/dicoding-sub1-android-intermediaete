package com.hirin.story.data.moment.response

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Keep
data class MomentListStoryResponse(
    @SerializedName("id") var id: String,
    @SerializedName("name") var name: String,
    @SerializedName("description") var description: String,
    @SerializedName("photoUrl") var photoUrl: String,
    @SerializedName("createdAt") var createdAt: String,
    @SerializedName("lat") var lat: Double,
    @SerializedName("lon") var lon: Double
): Parcelable