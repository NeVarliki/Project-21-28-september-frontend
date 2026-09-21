package ru.nto.storage.mobile.domain.auth

import ru.nto.storage.mobile.data.repo.AuthRepository

class CheckAndSaveAuthCodeUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(
        text: String
    ): Result<Unit> {
        return repository.checkAndSave(text).mapCatching { success ->
            if (!success) error("Code is incorrect")
        }
    }
}
