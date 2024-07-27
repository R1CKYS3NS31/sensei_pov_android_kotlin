package com.example.pov.ui.feature.home

import android.os.Build
import androidx.activity.compose.ReportDrawnWhen
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.data.data.model.pov.NewPoV
import com.example.pov.R
import com.example.pov.ui.design.component.pov.PoVAddDialog
import com.example.pov.ui.design.component.pov.PoVFab
import com.example.pov.ui.feature.pov.view_model.PoVUiState
import com.example.pov.ui.feature.pov.view_model.PoVViewModel
import com.example.pov.ui.theme.PoVTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.launch

@Composable
fun HomeRoute(
    navHostController: NavHostController,
    homeViewModel: HomeViewModel = hiltViewModel(),
    poVViewModel: PoVViewModel = hiltViewModel()

) {
    HomeScreen(
        homeViewModel = homeViewModel,
        poVViewModel = poVViewModel,
        navHostController = navHostController
    )
}

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
    poVViewModel: PoVViewModel,
    navHostController: NavHostController,
) {
    val homeUiState by homeViewModel.homeUiState.collectAsState()
    val poVUiState by poVViewModel.poVUiState.collectAsState(initial = PoVUiState.Success())

    val coroutineScope = rememberCoroutineScope()
    val isSyncing by homeViewModel.isSyncing.collectAsState()
    val isHomeUiStateLoading = homeUiState is HomeUiState.Loading
    val errorMessage by homeViewModel.errorMessage.collectAsState()
    val snackBarHostState = remember {
        SnackbarHostState()
    }
    var showPoVAddDialog by rememberSaveable {
        mutableStateOf(false)
    }

    ReportDrawnWhen {
        !isSyncing && !isHomeUiStateLoading
    }

    Scaffold(
        floatingActionButton = {
            PoVFab(
                modifier = Modifier, onClickPoVFab = {
                    showPoVAddDialog = true
                },
                icon = Icons.Filled.Add, text = R.string.create_pov
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        }
    ) { paddingValues ->
        AnimatedVisibility(
            visible = isSyncing || isHomeUiStateLoading,
            enter = slideInVertically(initialOffsetY = { fullHeight -> -fullHeight }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { fullHeight -> -fullHeight }) + fadeOut()
        ) {
            val loadingContentDescription = stringResource(id = R.string.loading)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 8.dp)
            ) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopCenter)
                )
            }
        }
        NotificationPermissionEffect()
        HomeBody(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues), homeUiState = homeUiState
        )
        if (showPoVAddDialog) {
            PoVAddDialog(
                modifier = Modifier,
                poVUiState = poVUiState,
                onValueChange = poVViewModel::addPoVUiState,
                onClickSave = {
                    coroutineScope.launch {
                        poVViewModel.savePoV(poVUiState.newPoV)
                    }
                },
                onDismissRequest = { showPoVAddDialog = false },
                onClear = {
                    /* clear */
                    coroutineScope.launch {
                        PoVUiState.Success(newPoV = NewPoV(author = ""), isEntryValid = false)
                        showPoVAddDialog = false
                    }
                },
                enableSave = poVUiState.isEntryValid
            )
        }


        LaunchedEffect(key1 = errorMessage) {
            if (errorMessage.isNotEmpty()) {
                snackBarHostState.showSnackbar(
                    message = errorMessage,
                    duration = SnackbarDuration.Long,
                    withDismissAction = true,
                    actionLabel = "OK"
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeBody(modifier: Modifier = Modifier, homeUiState: HomeUiState) {
    val state = rememberLazyGridState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Column(modifier = modifier.fillMaxSize()) {
        Text(
            text = stringResource(id = R.string.home),
            style = MaterialTheme.typography.headlineSmall
        )

        LazyVerticalGrid(
            columns = GridCells.Adaptive(200.dp),
            state = state,
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
        ) {
            poVGridList(
                homeUiState = homeUiState,
                onPoVClick = {},
            )
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class, ExperimentalPermissionsApi::class)
@Composable
private fun NotificationPermissionEffect() {
    // Permission requests should only be made from an Activity Context, which is not present
    // in previews
    if (LocalInspectionMode.current) return
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return
    val notificationsPermissionState = rememberPermissionState(
        android.Manifest.permission.POST_NOTIFICATIONS,
    )
    LaunchedEffect(notificationsPermissionState) {
        val status = notificationsPermissionState.status
        if (status is PermissionStatus.Denied && !status.shouldShowRationale) {
            notificationsPermissionState.launchPermissionRequest()
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PoVTheme {
        NotificationPermissionEffect()
    }
}