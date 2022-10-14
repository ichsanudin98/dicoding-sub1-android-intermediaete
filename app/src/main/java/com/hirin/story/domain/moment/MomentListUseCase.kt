package com.hirin.story.domain.moment

class MomentListUseCase(private val momentRepository: MomentRepository) {
    suspend operator fun invoke(
        page: Int,
        size: Int,
        location: Int
    ) = momentRepository.getMoment(page, size, location)
}