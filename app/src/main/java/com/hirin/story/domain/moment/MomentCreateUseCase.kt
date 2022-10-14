package com.hirin.story.domain.moment

import java.io.File

class MomentCreateUseCase(private val momentRepository: MomentRepository) {
    suspend operator fun invoke(
        photoFile: File,
        description: String,
        latitude: String?,
        longitude: String?
    ) = momentRepository.createMoment(photoFile, description, latitude, longitude)
}