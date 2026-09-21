package ru.nto.storage.mobile.ui.screen.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.nto.storage.mobile.data.repo.AuthRepository
import ru.nto.storage.mobile.data.repo.IssueRepository
import ru.nto.storage.mobile.domain.auth.LogoutUseCase
import ru.nto.storage.mobile.domain.main.GetMainDataUseCase
import ru.nto.storage.mobile.ui.nav.AuthScreenDestination
import ru.nto.storage.mobile.ui.nav.TakeScreenDestination
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class MainViewModel : ViewModel() {
    private val getMainDataUseCase by lazy {
        GetMainDataUseCase(IssueRepository(AuthRepository))
    }
    private val logoutUseCase by lazy {
        LogoutUseCase(AuthRepository)
    }
    private val _uiState = MutableStateFlow<MainState>(MainState.Loading)
    val uiState: StateFlow<MainState> = _uiState.asStateFlow()

    private val _actionFlow: MutableSharedFlow<MainAction> = MutableSharedFlow()
    val actionFlow: SharedFlow<MainAction> = _actionFlow

    init {
        refresh()
    }

    fun onIntent(intent: MainIntent) {
        when (intent) {
            is MainIntent.Take -> {
                viewModelScope.launch {
                    _actionFlow.emit(MainAction.Open(TakeScreenDestination))
                }
            }
            is MainIntent.Refresh -> {
                refresh()
            }
            is MainIntent.Logout -> {
                viewModelScope.launch {
                    logoutUseCase.invoke()
                    _actionFlow.emit(MainAction.Open(AuthScreenDestination, true))
                }
            }
        }
    }

    private fun refresh() {
        viewModelScope.launch {
            _uiState.update { MainState.Loading }
            _uiState.update {
                getMainDataUseCase.invoke().fold(
                    onSuccess = { data ->
                        MainState.Data(
                            name = data.name,
                            department = data.department,
                            photoUrl = data.photoUrl,
                            issues = data.issues.map { issue ->
                                MainState.Data.Issue(
                                    name = issue.name,
                                    inventoryCode = issue.inventoryCode,
                                    category = issue.category,
                                    returnDate = LocalDate
                                        .parse(issue.returnDate)
                                        .format(
                                            DateTimeFormatter.ofPattern(DATE_FORMAT)
                                        ),
                                )
                            }.toPersistentList()
                        )
                    },
                    onFailure = { error ->
                        MainState.Error(
                            error = error.message?.takeIf { it.isNotBlank() } ?: "Unknown error"
                        )
                    }
                )
            }
        }
    }

    private companion object {
        const val DATE_FORMAT = "dd.MM.yyyy"
    }
}
