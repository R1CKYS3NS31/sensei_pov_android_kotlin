package com.example.pov.ui.navigation.main

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.data.common.util.network.NetworkMonitor
import com.example.pov.ui.feature.auth.navigation.authScreen
import com.example.pov.ui.feature.auth.navigation.signInScreen
import com.example.pov.ui.feature.auth.navigation.signUpScreen
import com.example.pov.ui.feature.auth.navigation.welcomeNavigationRoute
import com.example.pov.ui.feature.auth.navigation.welcomeScreen
import com.example.pov.ui.feature.core.navigation.appScreen

const val rootNavigationRoute = "route_root"

@Composable
fun AppNavHost(
    navHostController: NavHostController,
    startDestination: String = welcomeNavigationRoute,
    networkMonitor: NetworkMonitor
) {
    NavHost(
        navController = navHostController,
        route = rootNavigationRoute,
        startDestination = startDestination
    ) {
        welcomeScreen(navHostController)
        authScreen(navHostController)
        signInScreen(navHostController)
        signUpScreen(navHostController)
        /* nested routes*/
        appScreen(networkMonitor)
        // TODO: implement call for the screens
    }
}