package ru.myitschool.storage.domain.main

import ru.myitschool.storage.data.repo.IssueRepository
import ru.myitschool.storage.domain.main.entities.MainInfoEntity
import java.time.LocalDate

class GetMainDataUseCase(
    private val repository: IssueRepository
) {
    suspend operator fun invoke(): Result<MainInfoEntity> {
        return repository.getInfo().map { main ->
            main.copy(
                issues = main.issues.sortedBy { issue ->
                    LocalDate.parse(issue.returnDate)
                }
            )
        }
    }
}
