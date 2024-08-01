package com.example.pov.ui.feature.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.data.common.util.network.NetworkMonitor
import com.example.pov.ui.feature.auth.navigation.signInScreen
import com.example.pov.ui.feature.auth.navigation.signUpScreen
import com.example.pov.ui.feature.catalog.navigation.catalogScreen
import com.example.pov.ui.feature.core.AppRoute
import com.example.pov.ui.feature.home.navigation.homeNavigationRoute
import com.example.pov.ui.feature.home.navigation.homeScreen
import com.example.pov.ui.feature.pov.navigation.poVAddEditScreen
import com.example.pov.ui.feature.profile.navigation.editProfileScreen
import com.example.pov.ui.feature.profile.navigation.profileScreen

const val appNavigationRoute = "app_route"
fun NavController.navigateToApp(navOptions: NavOptions? = null) {
    this.navigate(route = appNavigationRoute, navOptions = navOptions)
}

@Composable
fun MainNavHost(
    navHostController: NavHostController,
    onShowSnackbar: suspend (String, String?) -> Boolean
) {
    NavHost(
        navController = navHostController,
        startDestination = homeNavigationRoute,
        route = appNavigationRoute
    ) {
        signInScreen(navHostController)
        signUpScreen(navHostController)

        homeScreen(navHostController)
        catalogScreen(navHostController)
        profileScreen(navHostController)
        editProfileScreen(navHostController)

        poVAddEditScreen(navHostController)

    }
}

fun NavGraphBuilder.appScreen(networkMonitor: NetworkMonitor) {
    composable(route = appNavigationRoute) {
        AppRoute(networkMonitor)
    }
}