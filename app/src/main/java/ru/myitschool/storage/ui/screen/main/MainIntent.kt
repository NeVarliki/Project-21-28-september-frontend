package ru.myitschool.storage.ui.screen.main

sealed interface MainIntent {
    data object Refresh: MainIntent
    data object Logout: MainIntent
    data object Take: MainIntent
}
