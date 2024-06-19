package com.example.pov.ui.design.component.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.data.data.model.account.Name
import com.example.data.data.model.account.UserAccount

@Composable
fun ProfileEditForm(
    modifier: Modifier = Modifier,
    userAccount: UserAccount,
    onValueChange: (UserAccount) -> Unit,
    isError: Boolean = false
) {
    Column(modifier = modifier) {
        val value = remember {
            mutableStateOf("")
        }
        OutlinedTextField(
            value = userAccount.name.first.orEmpty(),
            onValueChange = { onValueChange(userAccount.copy(name = Name(first = it))) },
            label = {
                Text(
                    text = "First Name"
                )
            },
            singleLine = true,
            shape = MaterialTheme.shapes.large,
            isError = isError,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = userAccount.name.last.orEmpty(),
            onValueChange = { onValueChange(userAccount.copy(name = Name(last = it))) },
            label = {
                Text(
                    text = "Last Name"
                )
            },
            singleLine = true,
            shape = MaterialTheme.shapes.large,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = value.value,
            onValueChange = { value.value = it },
            label = {
                Text(
                    text = "Description"
                )
            },
            maxLines = 8,
            shape = MaterialTheme.shapes.large,
            modifier = Modifier.fillMaxWidth()
        )
    }
}