package ru.myitschool.storage.ui.screen.main

import kotlinx.collections.immutable.PersistentList

sealed interface MainState {
    data object Loading: MainState
    data class Error(
        val error: String
    ): MainState
    data class Data(
        val name: String,
        val department: String,
        val photoUrl: String,
        val issues: PersistentList<Issue>
    ): MainState {
        data class Issue(
            val name: String,
            val inventoryCode: String,
            val category: String,
            val returnDate: String,
        )
    }
}
