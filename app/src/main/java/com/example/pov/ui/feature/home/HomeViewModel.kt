package com.example.pov.ui.feature.home

import android.util.Log
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.common.result.ErrorResponse
import com.example.data.common.result.PoVResult
import com.example.data.common.util.datasync.SyncManager
import com.example.data.data.model.pov.PoV
import com.example.data.data.repository.pov.PoVRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

private const val TAG = "HomeViewModel"

@HiltViewModel
class HomeViewModel @Inject constructor(
    syncManager: SyncManager, poVRepository: PoVRepository
) : ViewModel() {

    val isSyncing = syncManager.isSyncing.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(TIME_IN_MILLIS),
        initialValue = false
    )

    /* error message */
    private val _errorMessage = MutableSharedFlow<String>()
    val errorMessage = _errorMessage.asSharedFlow().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(TIME_IN_MILLIS),
        initialValue = ""
    )


    val homeUiState: StateFlow<HomeUiState> by lazy {
        poVRepository.getAllPoVs().map { result: PoVResult<List<PoV>> ->
            when (result) {
                is PoVResult.Success -> {
                    HomeUiState.Success(povs = result.data)
                }

                is PoVResult.Error -> {
                    Log.d(
                        TAG,
                        "PoV getAllPoVs error: ${result.responseErrorMessage}",
                        result.throwable
                    )
                    _errorMessage.emit(
                        result.responseErrorMessage?.errorMessage ?: ""
                    )
                    HomeUiState.Error(
                        throwable = result.throwable,
                        responseErrorMessage = result.responseErrorMessage
                    )
                }

                is PoVResult.Loading -> {
                    HomeUiState.Loading
                }
            }

        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIME_IN_MILLIS),
            initialValue = HomeUiState.Loading
        )
    }


    companion object {
        private const val TIME_IN_MILLIS = 5_000L
    }
}

fun LazyGridScope.poVGridList(
    homeUiState: HomeUiState,
    onPoVClick: (String) -> Unit,
) {
    when (homeUiState) {
        HomeUiState.Loading -> Unit
        is HomeUiState.Error -> {
            Log.d(TAG, homeUiState.responseErrorMessage?.errorMessage ?: "")
        }

        is HomeUiState.Success -> {
            Log.d(TAG, "PoVs: ${homeUiState.povs.size}")
            items(items = homeUiState.povs, key = {
                it.id
            }, contentType = { "PoVs" }) { pov ->
                Text(text = "pov: ${pov.title}")
            }
        }
    }
}


sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Success(
        val povs: List<PoV> = emptyList(), val isEditEntryValid: Boolean = false
    ) : HomeUiState

    data class Error(
        val throwable: Throwable? = null, val responseErrorMessage: ErrorResponse? = null
    ) : HomeUiState
}