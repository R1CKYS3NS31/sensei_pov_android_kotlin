package com.example.pov.ui.feature.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.pov.ui.feature.home.HomeRoute

const val homeNavigationRoute = "home_route"

fun NavController.navigateToHome(navOptions: NavOptions? = null) {
    this.navigate(route = homeNavigationRoute, navOptions = navOptions)
}

fun NavGraphBuilder.homeScreen(
    navHostController: NavHostController,
    onShowSnackbar: suspend (String, String?) -> Boolean
) {
    composable(route = homeNavigationRoute) {
        HomeRoute(navHostController = navHostController, onShowSnackbar = onShowSnackbar) // from the feature screen
    }
}