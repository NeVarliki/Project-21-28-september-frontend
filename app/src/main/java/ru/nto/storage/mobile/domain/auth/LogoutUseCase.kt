package ru.nto.storage.mobile.domain.auth

import ru.nto.storage.mobile.data.repo.AuthRepository

class LogoutUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke() {
        repository.logout()
    }
}
