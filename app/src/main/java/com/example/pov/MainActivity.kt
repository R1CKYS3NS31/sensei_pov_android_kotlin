package com.example.pov

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.compose.rememberNavController
import com.example.data.common.util.network.NetworkMonitor
import com.example.pov.ui.feature.core.AppRoute
import com.example.pov.ui.navigation.main.AppNavHost
import com.example.pov.ui.theme.PoVTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var networkMonitor: NetworkMonitor

    private val viewModel by viewModels<MainActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        WindowCompat.setDecorFitsSystemWindows(window, false)

        var mainActivityUiState: MainActivityUiState by mutableStateOf(MainActivityUiState.Loading)

        /* update the mainActivityUiState*/
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.mainActivityUiState
                    .onEach { mainActivityUiState = it }
                    .collect()
            }
        }

//        installSplashScreen().apply {
//            setKeepOnScreenCondition {
//                /* set till app/viewModel is ready */
//                when (mainActivityUiState) {
//                    MainActivityUiState.Loading -> true
//                    is MainActivityUiState.Success -> false
//                }
//            }
//
//            setOnExitAnimationListener { splashScreen ->
//                val zoomX = ObjectAnimator.ofFloat(
//                    splashScreen.iconView,
//                    View.SCALE_X,
//                    0.4f,
//                    0.0f
//                )
//                zoomX.interpolator = OvershootInterpolator()
//                zoomX.duration = 500L
//                zoomX.doOnEnd { splashScreen.remove() }
//
//                val zoomY = ObjectAnimator.ofFloat(
//                    splashScreen.iconView,
//                    View.SCALE_Y,
//                    0.4f,
//                    0.0f
//                )
//                zoomY.interpolator = OvershootInterpolator()
//                zoomY.duration = 500L
//                zoomY.doOnEnd { splashScreen.remove() }
//
//                zoomX.start()
//                zoomY.start()
//            }
//        }

        setContent {
            PoVTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    val navHostController = rememberNavController()

                    when (isAuthenticated(mainActivityUiState)) {
                        true -> AppRoute(networkMonitor = networkMonitor)
                        false -> AppNavHost(
                            navHostController = navHostController,
                            networkMonitor = networkMonitor
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PoVTheme {
        Greeting("Sensei!")
    }
}

private fun isAuthenticated(
    mainActivityUiState: MainActivityUiState
): Boolean = when (mainActivityUiState) {
    MainActivityUiState.Loading -> false
    is MainActivityUiState.Success -> when (mainActivityUiState.authToken.isNotBlank()) {
        true -> true
        false -> false
    }
}