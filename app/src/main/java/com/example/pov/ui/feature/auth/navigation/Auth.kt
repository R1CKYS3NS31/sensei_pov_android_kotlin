package com.example.pov.ui.feature.auth.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.navigation

const val authNavigationRoute = "auth_route"

fun NavController.navigateToAuth(navOptions: NavOptions? = null) {
    this.navigate(route = authNavigationRoute, navOptions = navOptions)
}

fun NavGraphBuilder.authScreen(navHostController: NavHostController) {
    navigation(route = authNavigationRoute, startDestination = signInNavigationRoute) {
        signInScreen(navHostController)
        signUpScreen(navHostController)
    }
}