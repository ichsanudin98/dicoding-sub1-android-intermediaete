package com.hirin.story.domain.user

class RegisterUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(
        name: String,
        email: String,
        password: String
    ) = userRepository.register(name, email, password)
}