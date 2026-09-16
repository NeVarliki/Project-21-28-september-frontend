package ru.myitschool.storage.domain.auth

import ru.myitschool.storage.data.repo.AuthRepository

class LogoutUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke() {
        repository.logout()
    }
}
