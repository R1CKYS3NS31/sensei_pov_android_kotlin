package com.example.pov.ui.design.component.auth

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person2
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.example.pov.R

@Composable
fun NameTextField(
    modifier: Modifier = Modifier,
    nameTextFieldText: String,
    name: String,
    onValueChange: (String) -> Unit,
    isError: Boolean = false,
) {
    TextField(
        value = name,
        onValueChange = { onValueChange(it.trim()) },
        singleLine = true,
        label = {
            Text(
                text = nameTextFieldText,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        placeholder = {
            Text(text = nameTextFieldText)
        },
        shape = TextFieldDefaults.shape,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            errorContainerColor = Color.Transparent
        ),
        isError = isError,
        trailingIcon = @Composable {
            Icon(
                imageVector = Icons.Filled.Person2, contentDescription = nameTextFieldText
            )
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            /* imeAction */
            imeAction = ImeAction.Next,
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
fun EmailTextField(
    email: String,
    onValueChange: (String) -> Unit,
    isError: Boolean = false,
    modifier: Modifier
) {
    TextField(
        value = email,
        onValueChange = { onValueChange(it.trim()) },
        singleLine = true,
        label = {
            Text(
                text = stringResource(id = R.string.email),
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
                imageVector = Icons.Outlined.Email, contentDescription = stringResource(
                    id = R.string.email
                )
            )
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            errorContainerColor = Color.Transparent
        ),
        keyboardOptions = KeyboardOptions.Default.copy(
            /* imeAction */
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Email
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
fun PasswordTextField(
    modifier: Modifier,
    password: String,
    onValueChange: (String) -> Unit,
    isError: Boolean = false,
    onDoneImeAction: () -> Unit = {}
) {
    val showPassword = rememberSaveable {
        mutableStateOf(false)
    }
    TextField(
        value = password,
        onValueChange = { onValueChange(it) },
        singleLine = true,
        label = {
            Text(
                text = stringResource(id = R.string.password),
                style = MaterialTheme.typography.bodyMedium
            )
        },
        placeholder = {
            Text(text = stringResource(id = R.string.password_example))
        },
        shape = MaterialTheme.shapes.large,
        isError = isError,
        trailingIcon = {
            if (showPassword.value) {
                IconButton(onClick = { showPassword.value = false }) {
                    Icon(
                        imageVector = Icons.Filled.Visibility,
                        contentDescription = stringResource(
                            id = R.string.hide_password
                        )
                    )
                }
            } else {
                IconButton(onClick = { showPassword.value = true }) {
                    Icon(
                        imageVector = Icons.Outlined.VisibilityOff,
                        contentDescription = stringResource(
                            id = R.string.show_password
                        )
                    )
                }
            }
        },
        visualTransformation = if (showPassword.value) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            errorContainerColor = Color.Transparent
        ),
        keyboardOptions = KeyboardOptions.Default.copy(
            /* imeAction */
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Password
        ),
        keyboardActions = KeyboardActions(
            /* call the imeAction */
            onDone = {
                onDoneImeAction
            }
        ),
        modifier = modifier,
    )
}