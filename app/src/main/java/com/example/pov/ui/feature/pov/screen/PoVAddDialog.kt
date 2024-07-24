package com.example.pov.ui.feature.pov.screen

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.data.data.model.pov.NewPoV
import com.example.pov.R
import com.example.pov.ui.design.component.pov.PoVForm
import com.example.pov.ui.feature.pov.view_model.PoVUiState

@Composable
fun PoVAddDialog(
    modifier: Modifier = Modifier,
    poVUiState: PoVUiState.Success,
    onValueChange: (NewPoV) -> Unit,
    onClickSave: () -> Unit = {},
    onClear:()->Unit ={},
    enableSave: Boolean = false
) {
    PoVForm(
        modifier = Modifier.fillMaxSize(),
        newPoV = poVUiState.newPoV,
        onValueChange = onValueChange,
        isError = !poVUiState.isEntryValid
    )

    ElevatedCard(
        modifier = modifier,
        shape = MaterialTheme.shapes.extraLarge
    ) {
        Row {
            IconButton(
                onClick = onClear,
            ) {
                Icon(
                    imageVector = Icons.Filled.Cancel,
                    contentDescription = stringResource(id = R.string.cancel)
                )
                Text(text = stringResource(id = R.string.cancel))
            }

            IconButton(
                onClick = onClickSave,
                enabled = enableSave
            ) {
                Icon(
                    imageVector = Icons.Filled.Save,
                    contentDescription = stringResource(id = R.string.savePoV)
                )
                Text(text = stringResource(id = R.string.savePoV))
            }
        }
    }
}