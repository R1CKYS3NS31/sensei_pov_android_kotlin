package com.example.pov.ui.feature.auth.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.navOptions
import com.example.pov.R
import com.example.pov.ui.design.component.auth.WelcomeDisplay
import com.example.pov.ui.feature.auth.navigation.navigateToAuth
import kotlinx.coroutines.launch

@Composable
fun WelcomeRoute(navHostController: NavHostController) {
    WelcomeScreen(navHostController)
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun WelcomeScreen(navHostController: NavHostController) {
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        modifier = Modifier
            .semantics {
                testTagsAsResourceId = true
            }
            .fillMaxSize(),
        bottomBar = {
            Button(
                onClick = {
                    coroutineScope.launch {
                        navHostController.navigateToAuth(
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
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(text = stringResource(id = R.string.sign_in))
            }
        }
    ) { paddingValues ->
        WelcomeDisplay(
            modifier = Modifier.padding(paddingValues),
            headLine = stringResource(id = R.string.app_name)
        )
    }
}

//@Preview