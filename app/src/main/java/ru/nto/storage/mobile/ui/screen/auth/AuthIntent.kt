package ru.nto.storage.mobile.ui.screen.auth

sealed interface AuthIntent {
    data class Send(val text: String): AuthIntent
    data class TextInput(val text: String): AuthIntent
}
