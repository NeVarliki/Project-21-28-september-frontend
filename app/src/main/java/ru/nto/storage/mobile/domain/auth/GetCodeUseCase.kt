package ru.nto.storage.mobile.domain.auth

import ru.nto.storage.mobile.data.repo.AuthRepository

class GetCodeUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(): String? {
        return repository.getCode()
    }
}
