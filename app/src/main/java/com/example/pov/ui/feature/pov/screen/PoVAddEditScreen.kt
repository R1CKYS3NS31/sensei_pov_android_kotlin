package com.example.pov.ui.feature.pov.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.data.data.model.pov.PoV
import com.example.data.data.model.pov.asNewPoV
import com.example.pov.R
import com.example.pov.ui.design.component.pov.PoVForm
import com.example.pov.ui.design.component.pov.PoVTopAppBar
import com.example.pov.ui.feature.pov.view_model.PoVUiState
import com.example.pov.ui.feature.pov.view_model.PoVViewModel
import com.example.pov.ui.theme.PoVTheme
import kotlinx.coroutines.launch

@Composable
fun PovAddEditRoute(
    navHostController: NavHostController, viewModel: PoVViewModel = hiltViewModel()
) {
    PoVAddEditScreen(viewModel = viewModel, navHostController)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PoVAddEditScreen(viewModel: PoVViewModel, navHostController: NavHostController) {
    val poVUiState by viewModel.poVUiState.collectAsState(initial = PoVUiState.Success())
    val coroutineScope = rememberCoroutineScope()
    val snackBarHostState = remember {
        SnackbarHostState()
    }
    val errorMessage by viewModel.errorMessage.collectAsState()
    LaunchedEffect(key1 = errorMessage) {
        if (errorMessage.isNotBlank()) {
            snackBarHostState.showSnackbar(
                message = errorMessage, duration = SnackbarDuration.Long,
                withDismissAction = true,
                actionLabel = "OK"
            )
        }
    }
    Scaffold(
        topBar = {
            PoVTopAppBar(
                modifier = Modifier,
                actions = {
                    IconButton(
                        onClick = {
                            coroutineScope.launch {
                                viewModel.savePoV(poVUiState.poV.asNewPoV())
                            }
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Save,
                            contentDescription = stringResource(id = R.string.savePoV)
                        )
                    }
                    IconButton(
                        onClick = {
//                            DropdownMenu(expanded = true, onDismissRequest = { /*TODO*/ }) {
//                                DropdownMenuItem(
//                                    text = { Text(text = "Settings") },
//                                    onClick = { /*TODO*/ })
//                            }
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = null,
                        )
                    }

                },
                scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
                onClickBack = {
                    coroutineScope.launch {
                        viewModel.editPoV(poVUiState.poV)
                        navHostController.previousBackStackEntry
                    }
                },
            )
        }
    ) { paddingValues ->
        PoVAddEditBody(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            poVUiState = poVUiState,
            onValueChange = viewModel::editPoVUiState,
        )
    }
}

@Composable
fun PoVAddEditBody(
    modifier: Modifier = Modifier,
    poVUiState: PoVUiState.Success,
    onValueChange: (PoV) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(dimensionResource(id = R.dimen.padding_small))
            .verticalScroll(rememberScrollState())
    ) {
        PoVForm(
            modifier = Modifier,
            poV = poVUiState.poV,
            onValueChange = onValueChange,
            isError = !poVUiState.isEditEntryValid
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PoVTheme {
        PoVAddEditScreen(viewModel = hiltViewModel(), navHostController = rememberNavController())
    }
}