package ru.nto.storage.mobile.data.repo

import ru.nto.storage.mobile.data.dto.IssueRequestDto
import ru.nto.storage.mobile.data.source.NetworkDataSource
import ru.nto.storage.mobile.domain.main.entities.MainInfoEntity
import ru.nto.storage.mobile.domain.take.entities.EquipmentGroup
import ru.nto.storage.mobile.domain.take.entities.IssueRequestData

class IssueRepository(
    private val authRepository: AuthRepository
) {
    suspend fun getInfo(): Result<MainInfoEntity> {
        val code = authRepository.getCode() ?: return getNoAuthResult()
        return NetworkDataSource.getInfo(code).mapCatching { dto ->
            MainInfoEntity(
                name = dto.name ?: error("Name is null"),
                department = dto.department.orEmpty(),
                photoUrl = dto.photoUrl ?: error("Photo url is null"),
                issues = dto.issues?.mapNotNull { issue ->
                    val equipment = issue.equipment ?: return@mapNotNull null
                    MainInfoEntity.Issue(
                        id = issue.id ?: return@mapNotNull null,
                        name = equipment.name ?: return@mapNotNull null,
                        inventoryCode = equipment.inventoryCode.orEmpty(),
                        category = equipment.category.orEmpty(),
                        issueDate = issue.issueDate ?: return@mapNotNull null,
                        returnDate = issue.returnDate ?: return@mapNotNull null,
                    )
                } ?: listOf()
            )
        }
    }

    suspend fun getAvailableEquipment(): Result<List<EquipmentGroup>> {
        val code = authRepository.getCode() ?: return getNoAuthResult()
        return NetworkDataSource.getEquipment(code).mapCatching { dto ->
            dto?.map { (category, items) ->
                EquipmentGroup(
                    category = category,
                    items = items.mapNotNull { item ->
                        EquipmentGroup.Item(
                            id = item.id ?: return@mapNotNull null,
                            name = item.name ?: return@mapNotNull null,
                            inventoryCode = item.inventoryCode.orEmpty()
                        )
                    }
                )
            } ?: error("map is null")
        }
    }

    suspend fun createIssue(data: IssueRequestData): Result<Boolean> {
        val code = authRepository.getCode() ?: return getNoAuthResult()
        val dto = IssueRequestDto(data.equipmentId, data.returnDate)
        return NetworkDataSource.createIssue(code, dto)
    }

    private fun <T> getNoAuthResult() = Result.failure<T>(
        IllegalStateException("No auth")
    )
}
