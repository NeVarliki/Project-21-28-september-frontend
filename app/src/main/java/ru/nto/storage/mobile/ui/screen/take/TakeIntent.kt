package ru.nto.storage.mobile.ui.screen.take

sealed interface TakeIntent {
    data object Refresh: TakeIntent
    data class Take(
        val equipmentId: Long,
        val returnDate: String
    ): TakeIntent
}
