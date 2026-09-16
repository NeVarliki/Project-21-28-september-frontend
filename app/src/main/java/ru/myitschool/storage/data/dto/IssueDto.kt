package ru.myitschool.storage.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class IssueDto(
    @SerialName("id")
    val id: Long?,
    @SerialName("equipment")
    val equipment: EquipmentDto?,
    @SerialName("issueDate")
    val issueDate: String?,
    @SerialName("returnDate")
    val returnDate: String?,
)
