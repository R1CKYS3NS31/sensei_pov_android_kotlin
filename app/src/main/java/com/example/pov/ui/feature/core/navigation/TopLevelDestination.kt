package com.example.pov.ui.feature.core.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person3
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person3
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.pov.R

enum class TopLevelDestination(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val iconTextId: Int,
    val titleTextId: Int
) {
    HOME(
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
        iconTextId = R.string.home,
        titleTextId = R.string.app_name
    ),
    CATALOG(
        selectedIcon = Icons.Filled.Category,
        unselectedIcon = Icons.Outlined.Category,
        iconTextId = R.string.catalog,
        titleTextId = R.string.catalog
    ),
    PROFILE(
        selectedIcon = Icons.Filled.Person3,
        unselectedIcon = Icons.Outlined.Person3,
        iconTextId = R.string.profile,
        titleTextId = R.string.profile
    )
}