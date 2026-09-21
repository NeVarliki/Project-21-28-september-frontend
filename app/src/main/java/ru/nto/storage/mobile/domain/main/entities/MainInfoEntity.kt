package ru.nto.storage.mobile.domain.main.entities

data class MainInfoEntity(
    val name: String,
    val department: String,
    val photoUrl: String,
    val issues: List<Issue>
) {
    data class Issue(
        val id: Long,
        val name: String,
        val inventoryCode: String,
        val category: String,
        val issueDate: String,
        val returnDate: String,
    )
}
