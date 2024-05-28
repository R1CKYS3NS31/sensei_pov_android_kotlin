package com.example.pov

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.data.repository.account.UserAccountRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(
    private val userAccountRepository: UserAccountRepository
) : ViewModel() {
    val mainActivityUiState: StateFlow<MainActivityUiState> by lazy {
        userAccountRepository.authToken.map {
            MainActivityUiState.Success(it.orEmpty())
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = MainActivityUiState.Loading
        )
    }

    companion object {
        const val TIMEOUT_MILLIS = 5_000L
    }
}

sealed interface MainActivityUiState {
    data object Loading : MainActivityUiState
    data class Success(val authToken: String) : MainActivityUiState
}