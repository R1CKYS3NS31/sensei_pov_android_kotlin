package com.example.pov.ui.feature.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navOptions
import com.example.data.common.util.network.NetworkMonitor
import com.example.pov.ui.feature.catalog.navigation.CATALOG_NAVIGATION_ROUTE
import com.example.pov.ui.feature.catalog.navigation.navigateToCatalog
import com.example.pov.ui.feature.core.navigation.TopLevelDestination
import com.example.pov.ui.feature.home.navigation.homeNavigationRoute
import com.example.pov.ui.feature.home.navigation.navigateToHome
import com.example.pov.ui.feature.profile.navigation.navigateToProfile
import com.example.pov.ui.feature.profile.navigation.profileNavigationRoute
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@Composable
internal fun rememberAppState(
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    navHostController: NavHostController,
    networkMonitor: NetworkMonitor,
): AppState {
    return remember(coroutineScope, navHostController, networkMonitor) {
        AppState(coroutineScope, navHostController, networkMonitor)
    }
}

internal class AppState(
    coroutineScope: CoroutineScope,
    private val navHostController: NavHostController,
    networkMonitor: NetworkMonitor
) {

    val isOffline = networkMonitor.isOnline
        .map(Boolean::not)
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = false
        )

    val currentDestination: NavDestination?
        @Composable get() = navHostController.currentBackStackEntryAsState().value?.destination
    val currentTopLevelDestination: TopLevelDestination?
        @Composable get() = when (currentDestination?.route) {
            homeNavigationRoute -> TopLevelDestination.HOME
            CATALOG_NAVIGATION_ROUTE -> TopLevelDestination.CATALOG
            profileNavigationRoute -> TopLevelDestination.PROFILE
            /* more top level destinations */
            else -> null
        }
    val topLevelDestinations: List<TopLevelDestination> =
        TopLevelDestination.entries

    fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination) {
        val topLevelNavOptions = navOptions {
            popUpTo(navHostController.graph.findStartDestination().id) {
                saveState = true
                inclusive = true
            }
            launchSingleTop = true
            restoreState = true
        }

        when (topLevelDestination) {
            TopLevelDestination.HOME -> navHostController.navigateToHome(topLevelNavOptions)
            TopLevelDestination.CATALOG -> navHostController.navigateToCatalog(topLevelNavOptions)
            TopLevelDestination.PROFILE -> navHostController.navigateToProfile(topLevelNavOptions)
            /* more top level destinations*/
        }
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}