package com.example.pov.ui.design.component.pov

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.example.pov.R

@Composable
fun PoVFab(
    modifier: Modifier = Modifier,
    onClickPoVFab:  () -> Unit = {},
    icon: ImageVector = Icons.Filled.Add,
    text: Int = R.string.fab
) {
    ExtendedFloatingActionButton(
        modifier = modifier,
        onClick = { onClickPoVFab() },
        icon = { Icon(icon, stringResource(id = text)) },
        text = { Text(text = stringResource(id = text)) },
    )
}