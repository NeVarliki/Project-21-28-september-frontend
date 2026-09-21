package ru.nto.storage.mobile.domain.take

import ru.nto.storage.mobile.data.repo.IssueRepository
import ru.nto.storage.mobile.domain.take.entities.EquipmentGroup

class GetAvailableEquipmentUseCase(
    private val repository: IssueRepository
) {
    suspend operator fun invoke(): Result<List<EquipmentGroup>> {
        return repository.getAvailableEquipment().map { data ->
            data.filter { it.items.isNotEmpty() }
        }
    }
}
