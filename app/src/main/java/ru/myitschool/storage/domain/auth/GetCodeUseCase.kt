package ru.myitschool.storage.domain.auth

import ru.myitschool.storage.data.repo.AuthRepository

class GetCodeUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(): String? {
        return repository.getCode()
    }
}
