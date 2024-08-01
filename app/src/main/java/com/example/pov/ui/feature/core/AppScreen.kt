package com.example.pov.ui.feature.core

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.data.common.util.network.NetworkMonitor
import com.example.pov.R
import com.example.pov.ui.design.component.core.AppBarItem
import com.example.pov.ui.feature.core.navigation.MainNavHost
import com.example.pov.ui.feature.core.navigation.TopLevelDestination

@Composable
fun AppRoute(
    networkMonitor: NetworkMonitor,
    navHostController: NavHostController = rememberNavController()
) {
    AppScreen(
        navHostController = navHostController, networkMonitor = networkMonitor
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun AppScreen(
    navHostController: NavHostController,
    networkMonitor: NetworkMonitor,
    appState: AppState = rememberAppState(
        navHostController = navHostController,
        networkMonitor = networkMonitor
    )
) {
    val snackBarHostState = remember {
        SnackbarHostState()
    }
    val isOffline by appState.isOffline.collectAsState()
    val notConnectedMessage = stringResource(R.string.not_connected)
    LaunchedEffect(key1 = isOffline) {
        if (isOffline) {
            snackBarHostState.showSnackbar(
                message = notConnectedMessage, duration = SnackbarDuration.Indefinite
            )
        }
    }

    Scaffold(modifier = Modifier
        .semantics {
            testTagsAsResourceId = true
        }
        .fillMaxSize(),
        containerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.onBackground,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        },
        bottomBar = {
            AppBottomBar(
                destinations = appState.topLevelDestinations,
                onNavigateToDestination = appState::navigateToTopLevelDestination,
                currentDestination = appState.currentDestination
            )
        }
    ) { paddingValues ->
        Row(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .consumeWindowInsets(paddingValues)
                .windowInsetsPadding(
                    WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal)
                )
        ) {
            Column(Modifier.fillMaxSize()) {
                MainNavHost(
                    navHostController = navHostController,
                    onShowSnackbar = { message, action ->
                        snackBarHostState.showSnackbar(
                            message = message,
                            actionLabel = action,
                            duration = SnackbarDuration.Short,
                        ) == SnackbarResult.ActionPerformed
                    }
                )
            }
        }
    }
}


private fun NavDestination?.isTopLevelDestinationInHierarchy(destination: TopLevelDestination) =
    this?.hierarchy?.any {
        it.route?.contains(destination.name, true) ?: false
    } ?: false

@Composable
fun AppBottomBar(
    destinations: List<TopLevelDestination>,
    onNavigateToDestination: (TopLevelDestination) -> Unit,
    currentDestination: NavDestination?,
    modifier: Modifier = Modifier
) {
    val bottomBarDestinations =
        destinations.any { currentDestination.isTopLevelDestinationInHierarchy(it) }
    if (bottomBarDestinations) {
        com.example.pov.ui.design.component.core.AppNavigationBar(modifier) {
            destinations.forEach { destination ->
                val selected =
                    currentDestination.isTopLevelDestinationInHierarchy(destination = destination)
                AppBarItem(
                    selected = selected,
                    onClick = { onNavigateToDestination(destination) },
                    icon = {
                        Icon(
                            imageVector = destination.unselectedIcon,
                            contentDescription = stringResource(id = destination.iconTextId)
                        )
                    },
                    selectedIcon = {
                        Icon(
                            imageVector = destination.selectedIcon,
                            contentDescription = stringResource(id = destination.iconTextId)
                        )
                    },
                    label = { Text(text = stringResource(id = destination.iconTextId)) }
                )
            }
        }
    }
}