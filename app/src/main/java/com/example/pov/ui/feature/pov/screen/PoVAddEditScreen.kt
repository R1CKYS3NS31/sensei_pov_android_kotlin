package com.example.pov.ui.feature.pov.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.data.data.model.pov.PoV
import com.example.pov.Greeting
import com.example.pov.R
import com.example.pov.ui.design.component.pov.PoVForm
import com.example.pov.ui.feature.pov.view_model.PoVUiState
import com.example.pov.ui.feature.pov.view_model.PoVViewModel
import com.example.pov.ui.theme.PoVTheme

@Composable
fun PovAddEditRoute(
    navHostController: NavHostController, viewModel: PoVViewModel = hiltViewModel()
) {
    PoVAddEditScreen(viewModel = viewModel)
}

@Composable
fun PoVAddEditScreen(viewModel: PoVViewModel) {
    val poVUiState by viewModel.poVUiState.collectAsState(initial = PoVUiState.Success())

    Scaffold { paddingValues ->
        PoVAddEditBody(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
            poVUiState = poVUiState as PoVUiState.Success,
            onValueChange = viewModel::editPoVUiState,
            onClickBack = { },
            onClickSaveNew = { })
    }
}

@Composable
fun PoVAddEditBody(
    modifier: Modifier = Modifier,
    poVUiState: PoVUiState.Success,
    onValueChange: (PoV) -> Unit,
    onClickBack: () -> Unit = {},
    onClickSaveNew: () -> Unit = {}
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
        PoVAddEditScreen(viewModel = hiltViewModel())
    }
}