package ru.myitschool.storage.domain.take.entities

data class EquipmentGroup(
    val category: String,
    val items: List<Item>
) {
    data class Item(
        val id: Long,
        val name: String,
        val inventoryCode: String
    )
}
