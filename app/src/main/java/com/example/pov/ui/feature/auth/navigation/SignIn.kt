package com.example.pov.ui.feature.auth.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.pov.ui.feature.auth.screen.SignInRoute

const val signInNavigationRoute = "sign_in_route"

fun NavController.navigateToSignIn(navOptions: NavOptions? = null) {
    this.navigate(signInNavigationRoute, navOptions)
}

fun NavGraphBuilder.signInScreen(navHostController: NavHostController) {
    composable(route = signInNavigationRoute) {
        SignInRoute(navHostController)
    }
}