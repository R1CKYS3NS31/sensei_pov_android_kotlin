package com.example.pov.ui.design.component.pov

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.data.model.pov.NewPoV
import com.example.pov.R
import com.example.pov.ui.feature.pov.view_model.PoVUiState

@Composable
fun PoVAddDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    poVUiState: PoVUiState.Success,
    onValueChange: (NewPoV) -> Unit,
    onClickSave: () -> Unit = {},
    onClear: () -> Unit = {},
    enableSave: Boolean = false
) {
    Dialog(
        onDismissRequest = { onDismissRequest() },
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
        ),
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant),
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ){
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
        }
    }
}