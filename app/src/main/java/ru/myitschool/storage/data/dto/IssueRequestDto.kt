package ru.myitschool.storage.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class IssueRequestDto(
    @SerialName("equipmentId")
    val equipmentId: Long,
    @SerialName("returnDate")
    val returnDate: String,
)
