package com.example.pov.ui.feature.profile.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.pov.ui.feature.profile.EditProfileRoute
import com.example.pov.ui.feature.profile.ProfileRoute

const val profileNavigationRoute = "profile_route"
const val editProfileNavigationRoute = "edit_profile_route"

fun NavController.navigateToProfile(navOptions: NavOptions? = null) {
    this.navigate(route = profileNavigationRoute, navOptions = navOptions)
}

fun NavController.navigateToEditProfile(navOptions: NavOptions? = null) {
    this.navigate(route = editProfileNavigationRoute, navOptions = navOptions)
}

fun NavGraphBuilder.profileScreen(navHostController: NavHostController) {
    composable(route = profileNavigationRoute) {
        ProfileRoute(navHostController = navHostController)
    }
}

fun NavGraphBuilder.editProfileScreen(navHostController: NavHostController){
    composable(route = editProfileNavigationRoute){
        EditProfileRoute(navHostController)
    }
}