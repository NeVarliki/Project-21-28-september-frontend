package ru.myitschool.storage.ui.screen.main

import ru.myitschool.storage.ui.nav.AppDestination

sealed interface MainAction {
    class Open(
        val destination: AppDestination,
        val clearBackStack: Boolean = false
    ): MainAction
}
