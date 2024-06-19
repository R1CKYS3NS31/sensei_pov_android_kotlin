package com.example.pov.ui.feature.catalog.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.pov.ui.feature.catalog.CatalogRoute

const val CATALOG_NAVIGATION_ROUTE = "catalog_navigation_route"

fun NavHostController.navigateToCatalog(navOptions: NavOptions? = null) {
    this.navigate(route = CATALOG_NAVIGATION_ROUTE, navOptions = navOptions)
}

fun NavGraphBuilder.catalogScreen(navHostController: NavHostController) {
    composable(route = CATALOG_NAVIGATION_ROUTE) {
        CatalogRoute(navHostController = navHostController)
    }

}