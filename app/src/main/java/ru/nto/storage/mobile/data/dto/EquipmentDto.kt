package ru.nto.storage.mobile.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EquipmentDto(
    @SerialName("id")
    val id: Long?,
    @SerialName("name")
    val name: String?,
    @SerialName("inventoryCode")
    val inventoryCode: String?,
    @SerialName("category")
    val category: String?,
)
