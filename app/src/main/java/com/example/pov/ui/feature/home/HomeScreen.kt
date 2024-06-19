package com.example.pov.ui.feature.home

import androidx.activity.compose.ReportDrawnWhen
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.pov.R

@Composable
fun HomeRoute(
    navHostController: NavHostController,
    viewModel: HomeViewModel = hiltViewModel(),

    ) {
    HomeScreen(
        viewModel = viewModel,
    )
}

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
) {
    val homeUiState by viewModel.homeUiState.collectAsState()
    val isSyncing by viewModel.isSyncing.collectAsState()
    val isHomeUiStateLoading = homeUiState is HomeUiState.Loading
    val errorMessage by viewModel.errorMessage.collectAsState()
    val snackBarHostState = remember {
        SnackbarHostState()
    }

    LaunchedEffect(key1 = errorMessage) {
        if (errorMessage.isNotEmpty()) {
            snackBarHostState.showSnackbar(
                message = errorMessage, duration = SnackbarDuration.Long,
                withDismissAction = true,
                actionLabel = "OK"
            )
        }
    }

    ReportDrawnWhen {
        !isSyncing && !isHomeUiStateLoading
    }

    Scaffold { paddingValues ->

        HomeBody(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            homeUiState = homeUiState
        )

//        AnimatedVisibility(
//            visible = isSyncing || isHomeUiStateLoading,
//            enter = slideInVertically(
//                initialOffsetY = { fullHeight -> -fullHeight }
//            ) + fadeIn(),
//            exit = slideOutVertically(
//                targetOffsetY = { fullHeight -> -fullHeight }
//            ) + fadeOut()
//        ) {
//            val loadingContentDescription = stringResource(id = R.string.loading)
//            Box(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(top = 8.dp)
//            ) {
//                LinearProgressIndicator(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .align(Alignment.TopCenter)
//                )
//            }
//        }

//        NotificationPermissionEffect()
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
            modifier = Modifier
                .nestedScroll(scrollBehavior.nestedScrollConnection)
        ) {
            usersGridList(
                homeUiState = homeUiState,
                onUserClick = {},
            )
        }
    }
}


//@OptIn(ExperimentalPermissionsApi::class)
//@Composable
//private fun NotificationPermissionEffect() {
//    // Permission requests should only be made from an Activity Context, which is not present
//    // in previews
//    if (LocalInspectionMode.current) return
//    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return
//    val notificationsPermissionState = rememberPermissionState(
//        android.Manifest.permission.POST_NOTIFICATIONS,
//    )
//    LaunchedEffect(notificationsPermissionState) {
//        val status = notificationsPermissionState.status
//        if (status is PermissionStatus.Denied && !status.shouldShowRationale) {
//            notificationsPermissionState.launchPermissionRequest()
//        }
//    }
//}
