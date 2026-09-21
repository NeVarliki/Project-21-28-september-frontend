package ru.nto.storage.mobile.domain.take

import ru.nto.storage.mobile.data.repo.IssueRepository
import ru.nto.storage.mobile.domain.take.entities.IssueRequestData

class SendIssueRequestUseCase(
    private val repository: IssueRepository
) {
    suspend operator fun invoke(data: IssueRequestData): Result<Unit> {
        return repository.createIssue(data).mapCatching { success ->
            if (!success) error("Issue error")
        }
    }
}
