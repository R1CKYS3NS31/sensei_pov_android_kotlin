package com.example.pov.ui.feature.pov.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.pov.ui.feature.pov.view_model.PoVUiState
import com.example.pov.ui.feature.pov.view_model.PoVViewModel

@Composable
fun PovCreateRoute(
    navHostController: NavHostController,
    viewModel: PoVViewModel = hiltViewModel()
) {
    PoVCreateScreen(viewModel = viewModel)
}

@Composable
fun PoVCreateScreen(viewModel: PoVViewModel) {
    val poVUiState by viewModel.poVUiState.collectAsState()

    Scaffold { paddingValues ->
        PoVCreateBody(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues), poVUiState = poVUiState)
    }
}

@Composable
fun PoVCreateBody(modifier: Modifier, poVUiState: PoVUiState) {
    TODO("Not yet implemented")
}
