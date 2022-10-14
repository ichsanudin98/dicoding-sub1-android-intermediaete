package com.hirin.story.domain.user

class LoginUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(
        email: String,
        password: String
    ) = userRepository.login(email, password)
}