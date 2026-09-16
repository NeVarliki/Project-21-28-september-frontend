package ru.myitschool.storage.ui.screen.take

import kotlinx.collections.immutable.PersistentList

sealed interface TakeState {
    data object Loading : TakeState

    data object Empty : TakeState
    data class Error(
        val error: String
    ) : TakeState

    data class Data(
        val groups: PersistentList<Group>
    ) : TakeState {

        data class Group(
            val category: String,
            val items: PersistentList<Item>,
        )

        data class Item(
            val id: Long,
            val name: String,
            val inventoryCode: String,
        )
    }
}
