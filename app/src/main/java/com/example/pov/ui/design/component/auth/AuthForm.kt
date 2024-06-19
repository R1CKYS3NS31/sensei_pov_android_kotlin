package com.example.pov.ui.design.component.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.example.data.data.model.account.Name
import com.example.data.data.model.account.UserAccountSignIn
import com.example.data.data.model.account.UserAccountSignUp
import com.example.pov.R


@Composable
fun SigInForm(
    modifier: Modifier,
    userAccountSignIn: UserAccountSignIn,
    onValueChange: (UserAccountSignIn) -> Unit = {},
    isError: Boolean = false
) {
    Column(
        modifier = modifier.padding(
            vertical = dimensionResource(id = R.dimen.padding_extra_large),
            horizontal = dimensionResource(
                id = R.dimen.padding_medium
            )
        ),
        verticalArrangement = Arrangement.spacedBy(
            dimensionResource(id = R.dimen.padding_small)
        )
    ) {
        EmailTextField(
            email = userAccountSignIn.email,
            onValueChange = { onValueChange(userAccountSignIn.copy(email = it)) },
            isError = isError,
            modifier = Modifier
                .fillMaxWidth()
//                .border(border = BorderStroke(1.dp, brush = Brush.radialGradient()))
        )
        PasswordTextField(
            password = userAccountSignIn.password,
            onValueChange = { onValueChange(userAccountSignIn.copy(password = it)) },
            isError = isError,
            modifier = Modifier.fillMaxWidth()
        )
    }

}

@Composable
fun SignUpForm(
    modifier: Modifier = Modifier,
    userAccountSignUp: UserAccountSignUp,
    onValueChange: (UserAccountSignUp) -> Unit,
    isError: Boolean
) {
    Column(
        modifier = modifier.padding(
            vertical = dimensionResource(id = R.dimen.padding_extra_large),
            horizontal = dimensionResource(
                id = R.dimen.padding_medium
            )
        ),
        verticalArrangement = Arrangement.spacedBy(
            dimensionResource(id = R.dimen.padding_small)
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NameTextField(
                nameTextFieldText = stringResource(id = R.string.first_name),
                name = userAccountSignUp.name.first ?: "",
                onValueChange = { onValueChange(userAccountSignUp.copy(Name(first = it))) },
                modifier = Modifier
                    .fillMaxWidth(0.45f)
            )
            NameTextField(
                nameTextFieldText = stringResource(id = R.string.last_name),
                name = userAccountSignUp.name.last ?: "",
                onValueChange = { onValueChange(userAccountSignUp.copy(Name(last = it))) },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
            )
        }
        EmailTextField(
            email = userAccountSignUp.email,
            onValueChange = { onValueChange(userAccountSignUp.copy(email = it)) },
            isError = isError,
            modifier = Modifier
                .fillMaxWidth()
        )
        PasswordTextField(
            password = userAccountSignUp.password,
            onValueChange = { onValueChange(userAccountSignUp.copy(password = it)) },
            isError = isError,
            modifier = Modifier
                .fillMaxWidth(),
        )
    }
}