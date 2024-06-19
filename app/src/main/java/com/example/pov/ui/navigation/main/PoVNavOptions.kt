package com.example.pov.ui.navigation.main

import androidx.navigation.NavOptions
import androidx.navigation.navOptions

object PoVNavOptions {
    fun topLevelNavOptions(id: Int): NavOptions =
        navOptions {
            popUpTo(id) {
                saveState = true
                inclusive = true
            }
            launchSingleTop = true
            restoreState = true
        }
    fun topLevelNavOptionsExclusive(id: Int): NavOptions =
        navOptions {
            popUpTo(id) {
                saveState = true
                inclusive =false
            }
            launchSingleTop = true
            restoreState = true
        }
    fun navOptionsNoRestoreState(id: Int): NavOptions =
        navOptions {
            popUpTo(id) {
                saveState = true
                inclusive = true
            }
            launchSingleTop = true
            restoreState = false
        }

}