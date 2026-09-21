package ru.nto.storage.mobile.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class IssueRequestDto(
    @SerialName("equipmentId")
    val equipmentId: Long,
    @SerialName("returnDate")
    val returnDate: String,
)
