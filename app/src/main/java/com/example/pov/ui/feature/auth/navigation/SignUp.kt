package com.example.pov.ui.feature.auth.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.pov.ui.feature.auth.screen.SignUpRoute

const val signUpNavigationRoute = "sign_up_route"
fun NavController.navigateToSignUp(navOptions: NavOptions? = null) {
    this.navigate(signUpNavigationRoute, navOptions)
}

fun NavGraphBuilder.signUpScreen(navHostController: NavHostController) {
    composable(route = signUpNavigationRoute) {
        SignUpRoute(navHostController)
    }
}