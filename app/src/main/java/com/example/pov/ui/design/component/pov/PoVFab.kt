package com.example.pov.ui.design.component.pov

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.pov.R

@Composable
fun PoVCreateFab(modifier: Modifier = Modifier, onClickCreatePoV: () -> Unit = {}) {
    ExtendedFloatingActionButton(
        onClick = { onClickCreatePoV() },
        icon = { Icon(Icons.Filled.Add, stringResource(id = R.string.create_pov)) },
        text = { Text(text = stringResource(id = R.string.create_pov)) },
    )
}