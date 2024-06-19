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
import com.example.data.data.model.user.User
import com.example.data.data.repository.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    syncManager: SyncManager,
    userRepository: UserRepository
) : ViewModel() {

    val isSyncing = syncManager.isSyncing
        .stateIn(
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
        userRepository.getAllUsers().map { result ->
            when (result) {
                is PoVResult.Success -> {
                    HomeUiState.Success(users = result.data)
                }

                is PoVResult.Error -> {
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

fun LazyGridScope.usersGridList(
    homeUiState: HomeUiState,
    onUserClick: (String) -> Unit,
) {
    when (homeUiState) {
        HomeUiState.Loading -> Unit
        is HomeUiState.Error -> {
            Log.d("HomeViewModel", homeUiState.responseErrorMessage?.errorMessage ?: "")
        }

        is HomeUiState.Success -> {
            Log.d("HomeViewModel", "user accounts: ${homeUiState.users.size}")
            items(
                items = homeUiState.users,
                key = {
                    it.id.orEmpty()
                },
                contentType = { "userAccounts" }
            ) { user ->
                Text(text = "user: ${user.name}")
            }
        }
    }
}


sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Success(
        val users: List<User> = emptyList(),
    ) :
        HomeUiState

    data class Error(
        val throwable: Throwable? = null,
        val responseErrorMessage: ErrorResponse? = null
    ) : HomeUiState
}