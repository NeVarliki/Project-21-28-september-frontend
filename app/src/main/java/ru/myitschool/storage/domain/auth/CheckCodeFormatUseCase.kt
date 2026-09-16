package ru.myitschool.storage.domain.auth

class CheckCodeFormatUseCase {
    operator fun invoke(
        text: String
    ): Boolean {
        return text.length == 4 && text.all { char ->
            (char >= 'A' && char <= 'Z') || (char >= 'a' && char <= 'z') || char.isDigit()
        }
    }
}
