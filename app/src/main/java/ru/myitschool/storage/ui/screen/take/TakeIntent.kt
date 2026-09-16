package ru.myitschool.storage.ui.screen.take

sealed interface TakeIntent {
    data object Refresh: TakeIntent
    data class Take(
        val equipmentId: Long,
        val returnDate: String
    ): TakeIntent
}
