package ru.myitschool.storage.domain.take

import ru.myitschool.storage.data.repo.IssueRepository
import ru.myitschool.storage.domain.take.entities.IssueRequestData

class SendIssueRequestUseCase(
    private val repository: IssueRepository
) {
    suspend operator fun invoke(data: IssueRequestData): Result<Unit> {
        return repository.createIssue(data).mapCatching { success ->
            if (!success) error("Issue error")
        }
    }
}
