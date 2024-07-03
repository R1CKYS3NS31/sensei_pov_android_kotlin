package com.example.pov.ui.design.component.pov

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.pov.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PoVTopAppBar(
    modifier: Modifier = Modifier,
    title: Int = R.string.app_name,
    actions: @Composable RowScope.() -> Unit = {},
    onClickBack: () -> Unit = {},
    scrollBehavior: TopAppBarScrollBehavior? = TopAppBarDefaults.pinnedScrollBehavior(),
    colors: TopAppBarColors = TopAppBarDefaults.topAppBarColors(
        titleContentColor = MaterialTheme.colorScheme.background,
        containerColor = MaterialTheme.colorScheme.onBackground,
        actionIconContentColor = MaterialTheme.colorScheme.background,
        navigationIconContentColor = MaterialTheme.colorScheme.background,
    )
) {
    CenterAlignedTopAppBar(
        modifier = modifier
            .fillMaxWidth(),
//            .statusBarsPadding(),
        title = { stringResource(id = title) },
        colors = colors,
        actions = actions,
        navigationIcon = {
            IconButton(
                onClick = onClickBack,
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBackIos, contentDescription = null
                )
            }
        },
        scrollBehavior = scrollBehavior
    )
}