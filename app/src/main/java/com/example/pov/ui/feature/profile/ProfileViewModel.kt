package com.example.pov.ui.feature.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.common.result.ErrorResponse
import com.example.data.common.result.PoVResult
import com.example.data.data.model.account.UserAccount
import com.example.data.data.repository.account.UserAccountRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

private const val TAG = "ProfileViewModel"

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userAccountRepository: UserAccountRepository
) : ViewModel() {

    /* error message */
    private val _errorMessage = MutableSharedFlow<String>()
    val errorMessage = _errorMessage.asSharedFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIME_IN_MILLIS),
            initialValue = ""
        )

    private val _profileUiState = MutableSharedFlow<ProfileUiState>()
    val profileUiState: StateFlow<ProfileUiState> = _profileUiState.asSharedFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIME_IN_MILLIS),
            initialValue = ProfileUiState.Loading
        )
//    val profileUiState: StateFlow<ProfileUiState> by lazy {
//        val userCredentials = runBlocking {
//            userAccountRepository.getUserCredentials().first()
//        }
//        userAccountRepository.getUserAccount(userCredentials.first.orEmpty())
//            .map { userAccountResult: PoVResult<UserAccount> ->
//                when (userAccountResult) {
//                    is PoVResult.Success -> {
//                        ProfileUiState.Success(userAccountResult.data)
//                    }
//
//                    is PoVResult.Error -> {
//                        ProfileUiState.Error(
//                            throwable = userAccountResult.throwable,
//                            responseErrorMessage = userAccountResult.responseErrorMessage
//                        )
//                    }
//
//                    is PoVResult.Loading -> {
//                        ProfileUiState.Loading
//                    }
//                }
//            }.stateIn(
//                scope = viewModelScope,
//                started = SharingStarted.WhileSubscribed(TIME_IN_MILLIS),
//                initialValue = ProfileUiState.Success()
//            )
//    }

//    private val _profileEditUiState = MutableSharedFlow<ProfileEditUiState.Success>(
//        replay = 10,
//        onBufferOverflow = BufferOverflow.DROP_OLDEST
//    )
//    val profileEditUiState = _profileEditUiState.asSharedFlow()
//        .stateIn(
//            scope = viewModelScope,
//            started = SharingStarted.WhileSubscribed(TIME_IN_MILLIS),
//            initialValue = ProfileEditUiState.Success()
//        )

    fun updateProfileUiState(userAccount: UserAccount) {
        viewModelScope.launch {
            _profileUiState.emit(
                ProfileUiState.Success(
                    userAccount = userAccount,
                    isEditEntryValid = validateProfileInput(userAccount)
                )
            )
        }
    }

    private fun validateProfileInput(userAccount: UserAccount): Boolean =
        with(userAccount) {
            !(name.first.isBlank()
                    || name.last.isBlank())
        }

    fun updateUserAccount(userAccount: UserAccount) {
        if (validateProfileInput(userAccount)) {
            viewModelScope.launch {
                userAccountRepository.updateUserAccount(userAccount)
                    .map { result: PoVResult<UserAccount> ->
                        when (result) {
                            is PoVResult.Success -> {
                                _profileUiState.emit(
                                    ProfileUiState.Success(
                                        userAccount = result.data
                                    )
                                )
                                ProfileUiState.Success(result.data)
                            }

                            is PoVResult.Error -> {
                                Log.d(
                                    TAG,
                                    "profile edit error: ${result.responseErrorMessage}",
                                    result.throwable
                                )
                                _errorMessage.emit(
                                    result.responseErrorMessage?.errorMessage ?: ""
                                )
                                ProfileUiState.Error(
                                    throwable = result.throwable,
                                    responseErrorMessage = result.responseErrorMessage
                                )//
                            }

                            is PoVResult.Loading -> {
                                ProfileUiState.Loading
                            }
                        }
                    }.collect()
            }
        }
    }

    init {
        viewModelScope.launch {
            val userCredentials = runBlocking {
                userAccountRepository.getUserCredentials().first()
            }
            userAccountRepository.getUserAccount(userCredentials.first.orEmpty())
                .map { userAccountResult: PoVResult<UserAccount> ->
                    when (userAccountResult) {
                        is PoVResult.Success -> {
                            _profileUiState.emit(
                                ProfileUiState.Success(userAccountResult.data)
                            )
                        }

                        is PoVResult.Error -> {
                            _profileUiState.emit(
                                ProfileUiState.Error(
                                    throwable = userAccountResult.throwable,
                                    responseErrorMessage = userAccountResult.responseErrorMessage
                                )
                            )
                        }

                        PoVResult.Loading -> {
                            _profileUiState.emit(
                                ProfileUiState.Loading
                            )
                        }
                    }
                }.stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(TIME_IN_MILLIS),
                    initialValue = ProfileUiState.Loading
                ).collect()
        }
    }

    companion object {
        const val TIME_IN_MILLIS = 5_000L
    }
}

sealed interface ProfileUiState {
    data object Loading : ProfileUiState
    data class Success(
        val userAccount: UserAccount = UserAccount(),
        val isEditEntryValid: Boolean = false
    ) : ProfileUiState

    data class Error(
        val throwable: Throwable? = null,
        val responseErrorMessage: ErrorResponse? = null
    ) : ProfileUiState
}