package ru.nto.storage.mobile.ui.screen.main

sealed interface MainIntent {
    data object Refresh: MainIntent
    data object Logout: MainIntent
    data object Take: MainIntent
}
