package ru.nto.storage.mobile.ui.screen.take

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
import ru.nto.storage.mobile.domain.take.GetAvailableEquipmentUseCase
import ru.nto.storage.mobile.domain.take.SendIssueRequestUseCase
import ru.nto.storage.mobile.domain.take.entities.IssueRequestData

class TakeViewModel : ViewModel() {
    private val issueRepository by lazy { IssueRepository(AuthRepository) }
    private val getAvailableEquipmentUseCase by lazy { GetAvailableEquipmentUseCase(issueRepository) }
    private val sendIssueRequestUseCase by lazy { SendIssueRequestUseCase(issueRepository) }
    private val _uiState = MutableStateFlow<TakeState>(TakeState.Loading)
    val uiState: StateFlow<TakeState> = _uiState.asStateFlow()

    private val _actionFlow: MutableSharedFlow<TakeAction> = MutableSharedFlow()
    val actionFlow: SharedFlow<TakeAction> = _actionFlow

    init {
        refresh()
    }

    fun onIntent(intent: TakeIntent) {
        when (intent) {
            is TakeIntent.Refresh -> {
                refresh()
            }

            is TakeIntent.Take -> {
                viewModelScope.launch {
                    sendIssueRequestUseCase.invoke(
                        IssueRequestData(
                            equipmentId = intent.equipmentId,
                            returnDate = intent.returnDate
                        )
                    ).fold(
                        onSuccess = {
                            _actionFlow.emit(TakeAction.BackWithSuccess)
                        },
                        onFailure = { error ->
                            error.printStackTrace()
                        }
                    )
                }
            }
        }
    }

    private fun refresh() {
        viewModelScope.launch {
            _uiState.update { TakeState.Loading }
            _uiState.update {
                getAvailableEquipmentUseCase.invoke().fold(
                    onSuccess = { data ->
                        if (data.isEmpty()) {
                            TakeState.Empty
                        } else {
                            TakeState.Data(
                                groups = data.map { group ->
                                    TakeState.Data.Group(
                                        category = group.category,
                                        items = group.items.map { item ->
                                            TakeState.Data.Item(
                                                id = item.id,
                                                name = item.name,
                                                inventoryCode = item.inventoryCode
                                            )
                                        }.toPersistentList()
                                    )
                                }.toPersistentList()
                            )
                        }
                    },
                    onFailure = { error ->
                        TakeState.Error(
                            error = error.message.orEmpty()
                        )
                    }
                )
            }
        }
    }
}
