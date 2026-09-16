package ru.myitschool.storage.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    @SerialName("name")
    val name: String?,
    @SerialName("department")
    val department: String?,
    @SerialName("photoUrl")
    val photoUrl: String?,
    @SerialName("issues")
    val issues: List<IssueDto>?
)
