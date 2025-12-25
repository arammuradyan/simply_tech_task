package com.example.simplytask

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplytask.data.ActionItem
import com.example.simplytask.data.ActionType
import com.example.simplytask.data.NavBarItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed class UiState {
    data object Locked : UiState()
    data object ShowDialog : UiState()
    data object UnlockInProgress : UiState()
    data object UnlockSuccess : UiState()
    data object Unlocked : UiState()
}

class MainViewModel : ViewModel() {

    val navBarItems = listOf(
        NavBarItem(R.drawable.ic_main, R.string.home, true),
        NavBarItem(R.drawable.ic_car, R.string.vehicle),
        NavBarItem(R.drawable.ic_point, R.string.map),
        NavBarItem(R.drawable.ic_support, R.string.support),
        NavBarItem(R.drawable.ic_gear, R.string.settings)
    )

    val actionItems = listOf(
        ActionItem(R.drawable.ic_locked, R.string.lock, ActionType.LOCK),
        ActionItem(R.drawable.ic_unlock, R.string.unlock, ActionType.UNLOCK),
        ActionItem(R.drawable.ic_fun, R.string.climate,),
        ActionItem(R.drawable.ic_charge, R.string.charge),
        ActionItem(R.drawable.ic_lights, R.string.lights)
    )

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.Locked)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun actionItemClicked(actionType: ActionType) {
        when (actionType) {
            ActionType.UNLOCK -> if (_uiState.value == UiState.Locked) _uiState.update { UiState.ShowDialog }
            ActionType.LOCK -> if (_uiState.value == UiState.Unlocked) _uiState.update { UiState.Locked }
            else -> Unit
        }
    }

    fun positiveClicked() = viewModelScope.launch {
        _uiState.update { UiState.UnlockInProgress }
        delay(PROGRESS_DURATION)
        _uiState.update { UiState.UnlockSuccess }
        delay(SUCCESS_DURATION)
        _uiState.update { UiState.Unlocked }
    }

    fun negativeClicked() {
        _uiState.update { UiState.Locked }
    }

    companion object {
        const val PROGRESS_DURATION = 5000L
        const val SUCCESS_DURATION = 1000L
    }
}
