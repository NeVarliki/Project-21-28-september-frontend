package ru.nto.storage.mobile.ui.screen.take

sealed interface TakeAction {
    object Back: TakeAction
    object BackWithSuccess: TakeAction
}
