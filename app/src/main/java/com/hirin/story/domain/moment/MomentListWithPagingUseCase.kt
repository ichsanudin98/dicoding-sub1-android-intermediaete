package com.hirin.story.domain.moment

class MomentListWithPagingUseCase(private val momentRepository: MomentRepository) {
    operator fun invoke() = momentRepository.getMomentWithPaging()
}