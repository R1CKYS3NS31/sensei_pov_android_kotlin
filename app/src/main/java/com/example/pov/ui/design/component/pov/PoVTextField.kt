package com.example.pov.ui.design.component.pov

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.example.pov.R

@Composable
fun PoVTitle(
    title: String,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit,
    isError: Boolean = false,
) {
    TextField(
        value = title,
        onValueChange = { onValueChange(it) },
        singleLine = true,
        label = {
            Text(
                text = stringResource(id = R.string.title),
                style = MaterialTheme.typography.bodyMedium
            )
        },
        placeholder = {
            Text(text = stringResource(id = R.string.title))
        },
        shape = MaterialTheme.shapes.small,
        isError = isError,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            disabledBorderColor = Color.Transparent,
            errorBorderColor = Color.Transparent,
        ),
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next,
            /* imeAction */
//            imeAction = imeAction,
            keyboardType = KeyboardType.Text
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                /* call the imeAction */
                ImeAction.Next
            }
        ),
        modifier = modifier
    )
}

@Composable
fun PoVPoints(
    points: String,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit,
    isError: Boolean = false,
) {
    OutlinedTextField(
        value = points,
        onValueChange = { onValueChange(it) },
        label = {
            Text(
                text = stringResource(id = R.string.points),
                style = MaterialTheme.typography.bodyMedium
            )
        },
        placeholder = {
            Text(text = stringResource(id = R.string.points))
        },
        shape = MaterialTheme.shapes.large,
        isError = isError,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            disabledBorderColor = Color.Transparent,
            errorBorderColor = Color.Transparent,
        ),
        modifier = modifier
    )
}