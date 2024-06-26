package com.example.pov.ui.feature.pov.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.pov.ui.feature.pov.screen.PovAddEditRoute

const val poVCreateNavigationRoute = "pov_create_route"

fun NavController.navigateToPovCreate(navOptions: NavOptions? = null) {
    this.navigate(route = poVCreateNavigationRoute, navOptions = navOptions)
}

fun NavGraphBuilder.poVCreate(navHostController: NavHostController) {
    composable(route = poVCreateNavigationRoute) {
        PovAddEditRoute(navHostController = navHostController)
    }
}