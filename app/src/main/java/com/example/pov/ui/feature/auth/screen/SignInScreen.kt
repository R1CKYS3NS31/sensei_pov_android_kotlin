package com.example.pov.ui.feature.auth.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.navOptions
import com.example.data.data.model.account.UserAccountSignIn
import com.example.pov.R
import com.example.pov.ui.design.component.auth.ContinueAsGuestButton
import com.example.pov.ui.feature.auth.navigation.navigateToSignUp
import com.example.pov.ui.feature.auth.view_model.AuthViewModel
import com.example.pov.ui.feature.auth.view_model.SignInUiState
import com.example.pov.ui.feature.core.navigation.navigateToApp
import com.example.pov.ui.navigation.main.PoVNavOptions
import kotlinx.coroutines.launch

@Composable
fun SignInRoute(
    navHostController: NavHostController,
    viewModel: AuthViewModel = hiltViewModel()
) {
    SignInScreen(navHostController, viewModel)
}

@Composable
fun SignInScreen(navHostController: NavHostController, viewModel: AuthViewModel) {
    val coroutineScope = rememberCoroutineScope()
    val signInUiState by viewModel.signInUiState.collectAsState(initial = SignInUiState.Success())
    val isAuthenticatedUiState by viewModel.isAuthenticatedUiState.collectAsState()

    val snackBarHostState = remember {
        SnackbarHostState()
    }
    val errorMessage by viewModel.errorMessage.collectAsState()

    LaunchedEffect(key1 = errorMessage) {
        if (errorMessage.isNotBlank()) {
            snackBarHostState.showSnackbar(
                message = errorMessage, duration = SnackbarDuration.Long,
                withDismissAction = true,
                actionLabel = "OK"
            )
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            ContinueAsGuestButton {
                coroutineScope.launch {
                    navHostController.navigateToApp(
                        navOptions = PoVNavOptions.topLevelNavOptionsExclusive(
                            navHostController.graph.findStartDestination().id
                        )
                    )
                }
            }
        }
    ) { paddingValues ->
        SignInBody(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = paddingValues),
            signInUiState = signInUiState,
            onValueChange = viewModel::signInUserAccountUiState,
            onClickSignIn = {
                coroutineScope.launch {
                    viewModel.signInUserAccount(signInUiState.userAccountSignIn)
                    if (isAuthenticatedUiState.isNotBlank()) {
                        navHostController.navigateToApp(
                            navOptions = navOptions {
                                popUpTo(navHostController.graph.findStartDestination().id) {
                                    saveState = true
                                    inclusive = true
                                }
                                launchSingleTop = true
                                restoreState = false
                            }
                        )
                    }
                }
            },
            onClickForgotPassword = {
                /* navigate to forgot password screen */
            },
            onClickSignUp = {
                coroutineScope.launch {
                    navHostController.navigateToSignUp(
                        navOptions = navOptions {
                            popUpTo(navHostController.graph.id) {
                                saveState = true
                                inclusive = true
                            }
                            launchSingleTop = true
                            restoreState = false
                        }
                    )
                }
            },
        )
    }
}

@Composable
fun SignInBody(
    modifier: Modifier = Modifier,
    signInUiState: SignInUiState.Success,
    onValueChange: (UserAccountSignIn) -> Unit,
    onClickSignIn: () -> Unit = {},
    onClickForgotPassword: () -> Unit = {},
    onClickSignUp: () -> Unit = {},
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(
            dimensionResource(id = R.dimen.padding_extra_small)
        )
    ) {
        com.example.pov.ui.design.component.auth.WelcomeDisplay(
            modifier = Modifier,
            headLine = stringResource(id = R.string.sign_in)
        )

        /* auth form */
        com.example.pov.ui.design.component.auth.SigInForm(
            modifier = Modifier,
            userAccountSignIn = signInUiState.userAccountSignIn,
            onValueChange = onValueChange,
            isError = !signInUiState.isSignInEntryValid
        )


        /* sign in button */
        com.example.pov.ui.design.component.auth.AuthButton(
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.padding_medium))
                .fillMaxWidth(),
            authText = stringResource(id = R.string.sign_in),
            onClick = onClickSignIn,
            enabled = signInUiState.isSignInEntryValid
        )
        com.example.pov.ui.design.component.auth.PoVDivider(
            modifier = Modifier
                .padding(vertical = dimensionResource(id = R.dimen.padding_small)),
            headLine = stringResource(id = R.string.or),
            headLineTextStyle = MaterialTheme.typography.titleSmall
        )

        /* other auth buttons */
        com.example.pov.ui.design.component.auth.OtherAuthButtons(
            modifier = Modifier,
            onClickForgotPassword = onClickForgotPassword,
            onClickSign = onClickSignUp,
            signText = stringResource(id = R.string.create_account)
        )

    }
}

//@Preview(showSystemUi = true)
//@Composable
//fun SingInPreview() {
//    PoVTheme {
//        SignInScreen(navHostController = rememberNavController(), viewModel = hiltViewModel())
//    }
//}