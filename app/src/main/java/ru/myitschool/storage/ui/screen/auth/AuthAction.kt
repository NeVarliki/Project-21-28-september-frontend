package ru.myitschool.storage.ui.screen.auth

import ru.myitschool.storage.ui.nav.AppDestination

sealed interface AuthAction {
    class Open(val destination: AppDestination): AuthAction
}
