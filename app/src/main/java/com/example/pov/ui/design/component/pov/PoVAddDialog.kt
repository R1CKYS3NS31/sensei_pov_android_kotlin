package com.example.pov.ui.design.component.pov

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.SecureFlagPolicy
import com.example.data.data.model.pov.NewPoV
import com.example.pov.R
import com.example.pov.ui.feature.pov.view_model.PoVUiState

@OptIn(ExperimentalMaterial3Api::class)
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
    ModalBottomSheet(
        modifier = modifier,
        onDismissRequest = { onDismissRequest() },
        properties = ModalBottomSheetProperties(
            securePolicy = SecureFlagPolicy.Inherit,
            isFocusable = true,
            shouldDismissOnBackPress = true
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(id = R.dimen.padding_small)),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                modifier = Modifier,
                onClick = onClear,
            ) {
                Icon(
                    imageVector = Icons.Filled.Cancel,
                    contentDescription = stringResource(id = R.string.cancel)
                )
                Text(
                    text = stringResource(id = R.string.cancel),
                    modifier = Modifier.padding(start = dimensionResource(id = R.dimen.padding_extra_small)),
                )
            }

            Button(
                onClick = onClickSave,
                enabled = enableSave
            ) {
                Icon(
                    imageVector = Icons.Filled.Save,
                    contentDescription = stringResource(id = R.string.savePoV)
                )
                Text(
                    text = stringResource(id = R.string.savePoV),
                    modifier = Modifier.padding(start = dimensionResource(id = R.dimen.padding_extra_small)),
                )
            }
        }
        PoVForm(
            modifier = Modifier.fillMaxWidth(),
            newPoV = poVUiState.newPoV,
            onValueChange = onValueChange,
            isError = !poVUiState.isEntryValid
        )
    }
}