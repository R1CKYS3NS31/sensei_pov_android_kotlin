package com.example.pov.ui.feature.pov.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.common.result.ErrorResponse
import com.example.data.common.result.PoVResult
import com.example.data.data.model.pov.NewPoV
import com.example.data.data.model.pov.PoV
import com.example.data.data.model.pov.asNewPoV
import com.example.data.data.model.pov.asPoV
import com.example.data.data.repository.pov.PoVRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "PoVViewModel"

@HiltViewModel
class PoVViewModel @Inject constructor(
    private val poVRepository: PoVRepository
) : ViewModel() {
    /* error message */
    private val _errorMessage = MutableSharedFlow<String>()
    val errorMessage = _errorMessage.asSharedFlow().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(TIME_IN_MILLIS),
        initialValue = ""
    )

    private val _poVUiState = MutableSharedFlow<PoVUiState.Success>()
    val poVUiState: StateFlow<PoVUiState.Success> = _poVUiState.asSharedFlow().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(TIME_IN_MILLIS),
        initialValue = PoVUiState.Success()
    )

    fun addEditPoVUiState(poV: PoV) {
        viewModelScope.launch {
            _poVUiState.emit(
                PoVUiState.Success(
                    poV = poV,
                    isEntryValid = validatePoVInput(poV.asNewPoV())
                )
            )
        }
    }

    fun addPoVUiState(newPoV: NewPoV) {
        viewModelScope.launch {
            _poVUiState.emit(
                PoVUiState.Success(
                    newPoV.asPoV(),
                    isEntryValid = validatePoVInput(newPoV)
                )
            )
        }
    }

    private fun validatePoVInput(newPoV: NewPoV): Boolean =
        with(newPoV) {
            !(title.isBlank() || points.isBlank()
//                    || author.isBlank()
                    )
        }

    fun savePoV(newPoV: NewPoV) {
        if (validatePoVInput(newPoV)) {
            viewModelScope.launch {
                poVRepository.addPoV(newPoV)
                    .map { result: PoVResult<PoV> ->
                        when (result) {
                            is PoVResult.Success -> {
                                _poVUiState.emit(
                                    PoVUiState.Success(result.data)
                                )
                            }

                            is PoVResult.Error -> {
                                Log.d(
                                    TAG,
                                    "PoV updatePoV error: ${result.responseErrorMessage}",
                                    result.throwable
                                )
                                _errorMessage.emit(
                                    result.responseErrorMessage?.errorMessage ?: ""
                                )
//                                _poVUiState.emit(
//                                    PoVUiState.Error(
//                                        throwable = result.throwable,
//                                        responseErrorMessage = result.responseErrorMessage
//                                    )
//                                )
                            }

                            PoVResult.Loading -> {
//                                _poVUiState.emit(
//                                    PoVUiState.Loading
//                                )
                            }
                        }
                    }.collect()
            }
        }
    }


    fun addEditPoV(poV: PoV) {
        if (validatePoVInput(poV.asNewPoV())) {
            viewModelScope.launch {
                poVRepository.addEditPoV(poV.asNewPoV())
                    .map { result: PoVResult<PoV> ->
                        when (result) {
                            is PoVResult.Success -> {
                                _poVUiState.emit(
                                    PoVUiState.Success(result.data)
                                )
                            }

                            is PoVResult.Error -> {
                                Log.d(
                                    TAG,
                                    "PoV addEditPoV error: ${result.responseErrorMessage}",
                                    result.throwable
                                )
                                _errorMessage.emit(
                                    result.responseErrorMessage?.errorMessage ?: ""
                                )
//                                _poVUiState.emit(
//                                    PoVUiState.Error(
//                                        throwable = result.throwable,
//                                        responseErrorMessage = result.responseErrorMessage
//                                    )
//                                )
                            }

                            PoVResult.Loading -> {
//                                _poVUiState.emit(
//                                    PoVUiState.Loading
//                                )
                            }
                        }
                    }.collect()
            }
        }
    }

    companion object {
        const val TIME_IN_MILLIS = 5_000L
    }
}

sealed interface PoVUiState {
    data object Loading : PoVUiState
    data class Success(
        val poV: PoV = PoV(author = ""),
        val isEntryValid: Boolean = false
    ) : PoVUiState

    data class Error(
        val throwable: Throwable? = null, val responseErrorMessage: ErrorResponse? = null
    ) : PoVUiState
}