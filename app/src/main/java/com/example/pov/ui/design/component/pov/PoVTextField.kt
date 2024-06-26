package com.example.pov.ui.design.component.pov

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.RemoveRedEye
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
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
        onValueChange = { onValueChange(it.trim()) },
        singleLine = true,
        label = {
            Text(
                text = stringResource(id = R.string.title),
                style = MaterialTheme.typography.bodyMedium
            )
        },
        placeholder = {
            Text(text = stringResource(id = R.string.email_example))
        },
        shape = MaterialTheme.shapes.large,
        isError = isError,
        trailingIcon = @Composable {
            Icon(
                imageVector = Icons.Outlined.RemoveRedEye, contentDescription = stringResource(
                    id = R.string.title
                )
            )
        },
        colors = TextFieldDefaults.colors(),
        keyboardOptions = KeyboardOptions.Default.copy(
            /* imeAction */
            keyboardType = KeyboardType.Text
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                /* call the imeAction */
            }
        ),
        modifier = modifier
    )
}


@Composable
fun PoVSubtitle(
    subtitle: String,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit,
    isError: Boolean = false,
) {
    TextField(
        value = subtitle,
        onValueChange = { onValueChange(it.trim()) },
        maxLines = 2,
        label = {
            Text(
                text = stringResource(id = R.string.subtitle),
                style = MaterialTheme.typography.bodyMedium
            )
        },
        placeholder = {
            Text(text = stringResource(id = R.string.subtitle))
        },
        shape = MaterialTheme.shapes.large,
        isError = isError,
        trailingIcon = @Composable {
            Icon(
                imageVector = Icons.Outlined.RemoveRedEye,
                contentDescription = stringResource(
                    id = R.string.subtitle
                )
            )
        },
        colors = TextFieldDefaults.colors(),
        keyboardOptions = KeyboardOptions.Default.copy(
            /* imeAction */
            keyboardType = KeyboardType.Text
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                /* call the imeAction */
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
    TextField(
        value = points,
        onValueChange = { onValueChange(it.trim()) },
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
        colors = TextFieldDefaults.colors(),
        keyboardOptions = KeyboardOptions.Default.copy(
            /* imeAction */
            keyboardType = KeyboardType.Text
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                /* call the imeAction */
            }
        ),
        modifier = modifier
    )
}