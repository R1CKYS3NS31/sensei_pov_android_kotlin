package com.example.pov.ui.feature.pov.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.pov.ui.feature.pov.screen.PovAddEditRoute

const val poVAddEditNavigationRoute = "pov_create_route"

fun NavController.navigateToPovAddEdit(navOptions: NavOptions? = null) {
    this.navigate(route = poVAddEditNavigationRoute, navOptions = navOptions)
}

fun NavGraphBuilder.poVAddEditScreen(navHostController: NavHostController) {
    composable(route = poVAddEditNavigationRoute) {
        PovAddEditRoute(navHostController = navHostController)
    }
}