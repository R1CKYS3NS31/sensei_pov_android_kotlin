package com.example.pov.ui.design.component.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.pov.R

@Composable
fun AuthButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    enabled: Boolean = false,
    authText: String,
) {
    Button(
        onClick = {
            onClick()
        },
        enabled = enabled,
        modifier = modifier
    ) {
        Text(
            text = authText,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun OtherAuthButtons(
    modifier: Modifier = Modifier,
    onClickForgotPassword: () -> Unit = {},
    onClickSign: () -> Unit = {},
    signText: String
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(id = R.dimen.padding_medium)),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextButton(onClick = onClickForgotPassword) {
            Text(
                text = stringResource(id = R.string.forgot_password),
                style = MaterialTheme.typography.bodyLarge
            )
        }
        TextButton(onClick = onClickSign) {
            Text(text = signText, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
fun ContinueAsGuestButton(
    onClick: () -> Unit = {}
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Text(text = stringResource(id = R.string.continue_as_Guest))
    }
}