package com.example.pov.ui.feature.auth.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.navOptions
import com.example.data.data.model.account.UserAccountSignUp
import com.example.pov.R
import com.example.pov.ui.design.component.auth.AuthButton
import com.example.pov.ui.design.component.auth.ContinueAsGuestButton
import com.example.pov.ui.design.component.auth.PoVDivider
import com.example.pov.ui.design.component.auth.SignUpForm
import com.example.pov.ui.design.component.auth.WelcomeDisplay
import com.example.pov.ui.feature.auth.navigation.navigateToSignIn
import com.example.pov.ui.feature.auth.view_model.AuthViewModel
import com.example.pov.ui.feature.auth.view_model.SignUpUiState
import com.example.pov.ui.feature.core.navigation.navigateToApp
import kotlinx.coroutines.launch

@Composable
fun SignUpRoute(
    navHostController: NavHostController,
    viewModel: AuthViewModel = hiltViewModel()
) {
    SignUpScreen(navHostController, viewModel)
}

@Composable
fun SignUpScreen(navHostController: NavHostController, viewModel: AuthViewModel) {
    val coroutineScope = rememberCoroutineScope()
    val signUpUiState by viewModel.signUpUiState.collectAsState(initial = SignUpUiState.Success())
    val isAuthenticatedUiState by viewModel.isAuthenticatedUiState.collectAsState()

    val snackBarHostState = remember {
        SnackbarHostState()
    }
    val errorMessage by viewModel.errorMessage.collectAsState()


    Scaffold(
        bottomBar = {
            ContinueAsGuestButton {
                coroutineScope.launch {
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
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        }

    ) { paddingValues ->
        SignUpBody(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = paddingValues),
            signUpUiState = signUpUiState,
            onValueChange = viewModel::signUpUserAccountUiState,
            onClickSignUp = {
                coroutineScope.launch {
                    viewModel.saveUserAccount(signUpUiState.userAccountSignUp)
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
            onClickSignIn = {
                coroutineScope.launch {
                    navHostController.navigateToSignIn(
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
            }
        )
        LaunchedEffect(key1 = errorMessage) {
            if (errorMessage.isNotBlank()) {
                snackBarHostState.showSnackbar(
                    message = errorMessage, duration = SnackbarDuration.Long,
                    withDismissAction = true,
                    actionLabel = "OK"
                )
            }
        }
    }

}

@Composable
fun SignUpBody(
    modifier: Modifier = Modifier,
    signUpUiState: SignUpUiState.Success,
    onValueChange: (UserAccountSignUp) -> Unit,
    onClickSignUp: () -> Unit = {},
    onClickSignIn: () -> Unit = {}
) {
    Column(modifier = modifier) {
        WelcomeDisplay(
            modifier = Modifier,
            headLine = stringResource(id = R.string.sign_up)
        )

        /* auth form */
        SignUpForm(
            modifier = Modifier,
            userAccountSignUp = signUpUiState.userAccountSignUp,
            onValueChange = onValueChange,
            isError = !signUpUiState.isSignUpEntryValid
        )

        /* sign up button */
        AuthButton(
            authText = stringResource(id = R.string.sign_up),
            onClick = onClickSignUp,
            enabled = signUpUiState.isSignUpEntryValid,
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.padding_medium)),

            )
        PoVDivider(
            headLine = stringResource(id = R.string.or),
            headLineTextStyle = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(vertical = dimensionResource(id = R.dimen.padding_small))
        )
        TextButton(
            onClick = { onClickSignIn() },
            modifier = Modifier
                .align(Alignment.End)
        ) {
            Text(text = stringResource(id = R.string.already_have_an_account))
        }
    }
}

//@Preview(showSystemUi = true)
//@Composable
//fun SingUpPreview() {
//    PoVTheme {
//        SignUpScreen(navHostController = rememberNavController(), viewModel = hiltViewModel())
//    }
//}