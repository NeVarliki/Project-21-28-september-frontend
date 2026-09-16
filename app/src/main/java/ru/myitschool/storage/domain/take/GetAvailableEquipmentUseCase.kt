package ru.myitschool.storage.domain.take

import ru.myitschool.storage.data.repo.IssueRepository
import ru.myitschool.storage.domain.take.entities.EquipmentGroup

class GetAvailableEquipmentUseCase(
    private val repository: IssueRepository
) {
    suspend operator fun invoke(): Result<List<EquipmentGroup>> {
        return repository.getAvailableEquipment().map { data ->
            data.filter { it.items.isNotEmpty() }
        }
    }
}
