package ru.nto.storage.mobile.ui.screen.main

import ru.nto.storage.mobile.ui.nav.AppDestination

sealed interface MainAction {
    class Open(
        val destination: AppDestination,
        val clearBackStack: Boolean = false
    ): MainAction
}
