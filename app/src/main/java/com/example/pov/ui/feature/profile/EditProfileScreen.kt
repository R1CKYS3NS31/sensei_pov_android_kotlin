package com.example.pov.ui.feature.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.data.data.model.account.UserAccount
import com.example.pov.ui.design.component.profile.ProfileEditForm
import kotlinx.coroutines.launch

@Composable
fun EditProfileRoute(
    navHostController: NavHostController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    EditProfileScreen(
        navHostController = navHostController,
        viewModel = viewModel
    )
}

@Composable
fun EditProfileScreen(
    viewModel: ProfileViewModel,
    navHostController: NavHostController
) {
    val coroutineScope = rememberCoroutineScope()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val profileUiState by viewModel.profileUiState.collectAsState()

    val snackBarHostState = remember {
        SnackbarHostState()
    }
    LaunchedEffect(key1 = errorMessage) {
        if (errorMessage.isNotBlank()) {
            snackBarHostState.showSnackbar(
                message = errorMessage, duration = SnackbarDuration.Long,
                withDismissAction = true,
                actionLabel = "OK"
            )
        }
    }

    /* screen */
    when (profileUiState) {
        is ProfileUiState.Error -> {
            Text(text = "${(profileUiState as ProfileUiState.Error).responseErrorMessage?.errorMessage}")
        }

        ProfileUiState.Loading -> {
            Text(text = "Loading...")
        }

        is ProfileUiState.Success -> {
            EditProfileBody(
                modifier = Modifier.fillMaxSize(),
                profileUiState = profileUiState as ProfileUiState.Success,
                onValueChange = viewModel::updateProfileUiState,
                onClickSave = {
                    coroutineScope.launch {
                        viewModel.updateUserAccount((profileUiState as ProfileUiState.Success).userAccount)
                        /* navigate back to profile */
                        navHostController.navigateUp()
                    }
                },
                onClickBack = {
                    navHostController.navigateUp()
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileBody(
    modifier: Modifier = Modifier,
    profileUiState: ProfileUiState.Success,
    onValueChange: (UserAccount) -> Unit,
    onClickSave: () -> Unit = {},
    onClickBack: () -> Unit = {}
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(text = "Edit", style = MaterialTheme.typography.headlineSmall) },
                actions = {
                    IconButton(
                        onClick = onClickSave,
                        enabled = profileUiState.isEditEntryValid
                    ) {
                        Icon(imageVector = Icons.Filled.Save, contentDescription = null)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onClickBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBackIos,
                            contentDescription = null
                        )
                    }
                }
            )
        },
        bottomBar = {
            ElevatedButton(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            ) {
                Text(text = "Remove") // leave/remove/delete - group||account
            }
        }
    ) { paddingValues ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.padding(paddingValues)
        ) {
            ProfileEditForm(
                modifier = Modifier.padding(horizontal = 8.dp),
                userAccount = profileUiState.userAccount,
                onValueChange = onValueChange,
                isError = !profileUiState.isEditEntryValid
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun EditScreenPreview(modifier: Modifier = Modifier) {
    EditProfileScreen(
        viewModel = hiltViewModel(),
        navHostController = rememberNavController()
    )
}