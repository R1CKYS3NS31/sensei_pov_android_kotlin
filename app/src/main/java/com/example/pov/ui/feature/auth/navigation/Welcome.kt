package com.example.pov.ui.feature.auth.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.pov.ui.feature.auth.screen.WelcomeRoute

const val welcomeNavigationRoute = "welcome_route"

fun NavController.navigateToWelcome(navOptions: NavOptions? = null) {
    this.navigate(route = welcomeNavigationRoute, navOptions = navOptions)
}

fun NavGraphBuilder.welcomeScreen(navHostController: NavHostController) {
    composable(route = welcomeNavigationRoute) {
        WelcomeRoute(navHostController = navHostController)
    }
}