package ru.nto.storage.mobile.ui.screen.auth

import ru.nto.storage.mobile.ui.nav.AppDestination

sealed interface AuthAction {
    class Open(val destination: AppDestination): AuthAction
}
