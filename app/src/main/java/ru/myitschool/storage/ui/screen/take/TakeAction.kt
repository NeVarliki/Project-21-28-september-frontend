package ru.myitschool.storage.ui.screen.take

sealed interface TakeAction {
    object Back: TakeAction
    object BackWithSuccess: TakeAction
}
