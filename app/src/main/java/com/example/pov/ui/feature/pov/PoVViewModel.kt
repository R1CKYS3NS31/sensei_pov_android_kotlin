package com.example.pov.ui.feature.pov

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.common.result.ErrorResponse
import com.example.data.data.model.pov.PoV
import com.example.data.data.repository.pov.PoVRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

private const val TAG = "PoVViewModel"

@HiltViewModel
class PoVViewModel @Inject constructor(
    private val poVRepository: PoVRepository
) : ViewModel() {
    /* error message */
    private val _errorMessage = MutableSharedFlow<String>()
    val errorMessage = _errorMessage.asSharedFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIME_IN_MILLIS),
            initialValue = ""
        )

    companion object {
        const val TIME_IN_MILLIS = 5_000L
    }
}

sealed interface PoVUiState {
    data object Loading : PoVUiState
    data class Success(
        val povs: List<PoV> = emptyList(),
        val isEditEntryValid: Boolean = false
    ) : PoVUiState

    data class Error(
        val throwable: Throwable? = null,
        val responseErrorMessage: ErrorResponse? = null
    ) : PoVUiState
}