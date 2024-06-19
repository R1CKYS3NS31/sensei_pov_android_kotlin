package com.example.pov.ui.feature.auth.view_model

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.common.result.ErrorResponse
import com.example.data.common.result.PoVResult
import com.example.data.data.model.account.UserAccount
import com.example.data.data.model.account.UserAccountSignIn
import com.example.data.data.model.account.UserAccountSignUp
import com.example.data.data.model.account.asUserAccountSignIn
import com.example.data.data.model.account.asUserAccountSignUp
import com.example.data.data.repository.account.UserAccountRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userAccountRepository: UserAccountRepository
) : ViewModel() {

    /* sign up user */
    private val _signUpUiState = MutableSharedFlow<SignUpUiState.Success>(
        replay = 10,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val signUpUiState: SharedFlow<SignUpUiState.Success> =
        _signUpUiState.asSharedFlow()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIME_IN_MILLIS),
                initialValue = SignUpUiState.Success()
            )// TODO: implement ui states - loading/error/success on the screen

    /* navigate if authed */
    val isAuthenticatedUiState: StateFlow<String> by lazy {
        userAccountRepository.authToken.map {
            it ?: ""
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(TIME_IN_MILLIS),
            ""
        )
    }


    /* error message */
    private val _errorMessage = MutableSharedFlow<String>()
    val errorMessage = _errorMessage.asSharedFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIME_IN_MILLIS),
            initialValue = ""
        )

    fun signUpUserAccountUiState(userAccountSignUp: UserAccountSignUp) {
        viewModelScope.launch {
            _signUpUiState.emit(
                SignUpUiState.Success(
                    userAccountSignUp = userAccountSignUp,
                    isSignUpEntryValid = validateSignUpInput(userAccountSignUp)
                )
            )
        }
    }

    private fun validateSignUpInput(userAccountSignUp: UserAccountSignUp): Boolean =
        with(userAccountSignUp) {
            !(name.first.isNullOrBlank()
                    || name.last.isNullOrBlank()
                    || email.isBlank()
                    || password.isBlank())
                    && Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }

    fun saveUserAccount(userAccountSignUp: UserAccountSignUp) {
        if (validateSignUpInput(userAccountSignUp)) {
            viewModelScope.launch {
                Log.d("AuthViewModel", "save called...$userAccountSignUp")

                /* save user in the repo*/
                userAccountRepository.signUp(userAccountSignUp)
                    .map { result: PoVResult<UserAccount> ->
                        when (result) {
                            is PoVResult.Success -> {
                                SignUpUiState.Success(result.data.asUserAccountSignUp())
                            }

                            is PoVResult.Error -> {

                                Log.d(
                                    "AuthViewModel",
                                    "sign up error: ${result.responseErrorMessage}",
                                    result.throwable
                                )
                                _errorMessage.emit(
                                    result.responseErrorMessage?.errorMessage ?: ""
                                )
                                SignUpUiState.Error(
                                    throwable = result.throwable,
                                    responseErrorMessage = result.responseErrorMessage
                                )//
                            }

                            is PoVResult.Loading -> {
                                SignUpUiState.Loading
                            }
                        }
                    }.collect()
            }
        }
    }

    /* sign in user */
    private val _signInUiState = MutableSharedFlow<SignInUiState.Success>(
        replay = 10,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val signInUiState: SharedFlow<SignInUiState.Success> =
        _signInUiState.asSharedFlow()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIME_IN_MILLIS),
                initialValue = SignInUiState.Success()
            )


    fun signInUserAccountUiState(userAccountSignIn: UserAccountSignIn) {
        viewModelScope.launch {
            _signInUiState.emit(
                SignInUiState.Success(
                    userAccountSignIn = userAccountSignIn,
                    isSignInEntryValid = validateSignInInput(userAccountSignIn)
                )
            )
        }
    }

    private fun validateSignInInput(userAccountSignIn: UserAccountSignIn): Boolean =
        with(userAccountSignIn) {
            !(email.isBlank()
                    || password.isBlank())
                    && Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }

    fun signInUserAccount(userAccountSignIn: UserAccountSignIn) {
        if (validateSignInInput(userAccountSignIn)) {
            Log.d("AuthViewModel", "save $userAccountSignIn")
            viewModelScope.launch {
                /* sign in on the auth repository */
                userAccountRepository.signIn(userAccountSignIn)
                    .map { result: PoVResult<UserAccount> ->
                        when (result) {
                            is PoVResult.Success -> {
                                SignInUiState.Success(result.data.asUserAccountSignIn())
                            }

                            is PoVResult.Error -> {
                                Log.d(
                                    "AuthViewModel",
                                    "sign in error: ${result.responseErrorMessage}",
                                    result.throwable
                                )

                                _errorMessage.emit(
                                    result.responseErrorMessage?.errorMessage ?: ""
                                )
                                SignInUiState.Error(
                                    throwable = result.throwable,
                                    responseErrorMessage = result.responseErrorMessage
                                )
                            }

                            is PoVResult.Loading -> {
                                SignInUiState.Loading
                            }
                        }
                    }.collect()
            }
        }
    }

    companion object {
        private const val TIME_IN_MILLIS = 5_000L
    }
}

sealed interface SignUpUiState {
    data object Loading : SignUpUiState
    data class Success(
        val userAccountSignUp: UserAccountSignUp = UserAccountSignUp(),
        val isSignUpEntryValid: Boolean = false
    ) :
        SignUpUiState

    data class Error(
        val throwable: Throwable? = null,
        val responseErrorMessage: ErrorResponse? = null
    ) : SignUpUiState
}

sealed interface SignInUiState {
    data object Loading : SignInUiState
    data class Success(
        val userAccountSignIn: UserAccountSignIn = UserAccountSignIn(),
        val isSignInEntryValid: Boolean = false
    ) :
        SignInUiState

    data class Error(
        val throwable: Throwable? = null,
        val responseErrorMessage: ErrorResponse? = null
    ) : SignInUiState
}