package com.example.pov.ui.feature.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.ImageOnly
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.VisualMediaType
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material.icons.automirrored.outlined.LiveHelp
import androidx.compose.material.icons.filled.Audiotrack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.PermMedia
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Sms
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.PrivacyTip
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import coil.compose.AsyncImage
import com.example.data.data.model.account.UserAccount
import com.example.pov.R
import com.example.pov.ui.design.component.pov.PoVFab
import com.example.pov.ui.feature.auth.navigation.navigateToSignIn
import com.example.pov.ui.feature.profile.navigation.navigateToEditProfile
import kotlinx.coroutines.launch

@Composable
fun ProfileRoute(
    navHostController: NavHostController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    ProfileScreen(
        navHostController = navHostController,
        viewModel = viewModel
    )
}

@Composable
fun ProfileScreen(
    navHostController: NavHostController,
    viewModel: ProfileViewModel
) {
    val profileUiState by viewModel.profileUiState.collectAsState()

    var selectedMedia by remember {
        mutableStateOf(Uri.EMPTY)
    }
    val filter by remember {
        mutableStateOf<VisualMediaType>(ImageOnly)
    }
    val pickMultipleMedia =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia()) {
            selectedMedia = it
        }
    val coroutineScope = rememberCoroutineScope()

    /* screen */
    when (profileUiState) {
        is ProfileUiState.Error -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center
            ) {
                TextButton(
                    onClick = {
                        coroutineScope.launch {
                            navHostController.navigateToSignIn(
                                navOptions {
                                    popUpTo(navHostController.graph.findStartDestination().id) {
                                        saveState = false
                                        inclusive = true
                                    }
                                    launchSingleTop = true
                                    restoreState = false
                                    popUpToRoute
                                }
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = dimensionResource(id = R.dimen.padding_small))
                        .align(Alignment.CenterHorizontally)
                ) {
                    Text(text = stringResource(R.string.please_sign_in))
                }
            }

        }

        ProfileUiState.Loading -> {
            Text(text = "Loading...")
        }

        is ProfileUiState.Success -> {
            ProfileBody(
                userAccount = (profileUiState as ProfileUiState.Success).userAccount,
                selectedMedia = selectedMedia,
                onClickPicker = {
                    pickMultipleMedia.launch(
                        PickVisualMediaRequest(filter)
                    )
                },
                onClickEdit = {
                    coroutineScope.launch {
                        navHostController.navigateToEditProfile()
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileBody(
    userAccount: UserAccount,
    selectedMedia: Uri,
    onClickPicker: () -> Unit,
    onClickEdit: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            PoVFab(
                modifier = Modifier,
                onClickPoVFab = {},
                icon = Icons.Filled.Sms,
                text = R.string.message
            )
        },
    ) { paddingValues ->

        LazyColumn(
            contentPadding = paddingValues, modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            /*  AsyncImage(
        model = "https://example.com/image.jpg",
        contentDescription = "Translated description of what the image contains"
    )*/

            item {
                ProfileHead(
                    modifier = Modifier,
                    userAccount = userAccount,
                    selectedMedia = selectedMedia,
                    onClickPicker = onClickPicker,
                )
            }
            item {
                ProfileNavBar(
                    modifier = Modifier,
                    userAccount = userAccount,
                    onClickEdit = onClickEdit
                )
            }
            item {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(
                        16.dp
                    )
                ) {
                    TextButton(
                        onClick = { /*TODO*/ },
                    ) {
                        Text(text = "07123456789")
                    }

                    TextButton(onClick = { /*TODO*/ }) {
                        Text(text = userAccount.email.orEmpty())
                    }
                }
            }
            item {
                Text(text = "Bio status")
            }

            item {
                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    shape = MaterialTheme.shapes.extraLarge
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Icon(imageVector = Icons.Outlined.Group, contentDescription = "group type")
                        Column {
                            Text(text = "Group Type", style = MaterialTheme.typography.bodyLarge)
                            Text(text = "Public", style = MaterialTheme.typography.labelMedium)
                        }
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = "group types"
                        )
                    }
                }
            }

            item {
                AppSettings(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
            }

            item {
                InstitutionSettings(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                )
            }

            item {
                TabRow(selectedTabIndex = 0) {
                    /* if group */
//                    Tab(
//                        selected = true,
//                        onClick = { /*TODO*/ },
//                        text = { Text(text = "Members") },
//                        icon = {
//                            Icon(
//                                imageVector = Icons.Filled.CardMembership,
//                                contentDescription = null
//                            )
//                        }
//                    )
                    Tab(selected = true,
                        onClick = { /*TODO*/ },
                        text = { Text(text = "Media") },
                        icon = {
                            Icon(imageVector = Icons.Filled.PermMedia, contentDescription = null)
                        })
                    Tab(selected = false,
                        onClick = { /*TODO*/ },
                        text = { Text(text = "Files") },
                        icon = {
                            Icon(imageVector = Icons.Filled.Newspaper, contentDescription = null)
                        })
                    Tab(selected = false,
                        onClick = { /*TODO*/ },
                        text = { Text(text = "Links") },
                        icon = {
                            Icon(imageVector = Icons.Filled.Link, contentDescription = null)
                        })
                    Tab(selected = false,
                        onClick = { /*TODO*/ },
                        text = { Text(text = "Audio") },
                        icon = {
                            Icon(imageVector = Icons.Filled.Audiotrack, contentDescription = null)
                        })
                }
            }

//            item {
//                LazyVerticalGrid(columns = GridCells.Adaptive(300.dp)) {
//                    /*TODO*/
//                }
//            }
        }
    }
}

@Composable
fun ProfileNavBar(
    modifier: Modifier,
    userAccount: UserAccount,
    onClickEdit: () -> Unit = {}
) {
    NavigationBar(
        modifier = modifier,
        containerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.primary
    ) {
        NavigationBarItem(selected = false, onClick = { /*TODO*/ }, icon = {
            Icon(
                imageVector = Icons.Filled.Call, contentDescription = null
            )
        }, alwaysShowLabel = true, label = {
            Text(text = "call")
        })
        NavigationBarItem(selected = false, onClick = { /*TODO*/ }, icon = {
            Icon(
                imageVector = Icons.Filled.Share, contentDescription = null
            )
        }, alwaysShowLabel = true, label = {
            Text(text = "share account")
        })
        NavigationBarItem(selected = true, onClick = { /*TODO*/ }, // common/mine
            icon = {
                Icon(
                    imageVector = Icons.Filled.Groups, contentDescription = null
                )
            }, alwaysShowLabel = true, label = {
                Text(text = "groups")
            })
        NavigationBarItem(selected = false, onClick = onClickEdit,
            icon = {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "edit user account"
                )
            },
            label = {
                Text(text = "user")
            }
        )
    }
}

@Composable
fun ProfileHead(
    modifier: Modifier = Modifier,
    userAccount: UserAccount,
    selectedMedia: Uri,
    onClickPicker: () -> Unit = {},
) {
    //            CircularPhoto(modifier = Modifier)
    FillWidthPhoto(
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp),
        userAccount = userAccount,
        selectedMedia = selectedMedia,
        onClickPicker = onClickPicker,
    )
}

@Composable
fun FillWidthPhoto(
    modifier: Modifier = Modifier,
    userAccount: UserAccount,
    selectedMedia: Uri,
    onClickPicker: () -> Unit = {},
) {
    Box(
        modifier = modifier
            .background(MaterialTheme.colorScheme.primaryContainer)
    ) {
        AsyncImage(
            model = selectedMedia,
            placeholder = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "profile pic",
            contentScale = ContentScale.Crop,
            fallback = painterResource(id = R.drawable.ic_launcher_foreground),
            error = painterResource(id = R.drawable.ic_launcher_foreground),
            modifier = Modifier.fillMaxWidth()
        )

        Column(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomStart),
        ) {
            Text(
                text = userAccount.name.first,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.background,

                )
            Text(
                text = "last seen recently",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.background,

                )
        }

        IconButton(
            onClick = onClickPicker,
            colors = IconButtonDefaults.iconButtonColors(
                contentColor = MaterialTheme.colorScheme.background,
                containerColor = Color.Transparent
            ),
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomEnd),
        ) {
            Icon(imageVector = Icons.Filled.Camera, contentDescription = "upload photo")
        }
    }
}

@Composable
fun AppSettings(modifier: Modifier = Modifier) {
    ElevatedCard(
        modifier = modifier,
        shape = MaterialTheme.shapes.extraLarge
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.padding_medium)),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = Icons.Outlined.ChatBubbleOutline,
                contentDescription = "chat settings"
            )
            Text(text = "Chat settings", style = MaterialTheme.typography.bodyMedium)
            Icon(
                imageVector = Icons.Filled.Edit,
                contentDescription = "edit"
            )
        }
        HorizontalDivider()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = Icons.Outlined.Security,
                contentDescription = "privacy and security"
            )
            Text(
                text = "Privacy & Security",
                style = MaterialTheme.typography.bodyMedium
            )
            Icon(
                imageVector = Icons.Filled.Edit,
                contentDescription = "edit"
            )
        }
        HorizontalDivider()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = Icons.Outlined.Notifications,
                contentDescription = "notification settings"
            )
            Text(
                text = "Notification settings",
                style = MaterialTheme.typography.bodyMedium
            )
            Icon(
                imageVector = Icons.Filled.Edit,
                contentDescription = "edit"
            )
        }
    }
}

@Composable
fun InstitutionSettings(modifier: Modifier = Modifier) {
    ElevatedCard(
        modifier = modifier,
        shape = MaterialTheme.shapes.extraLarge
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.LiveHelp,
                contentDescription = "live help"
            )
            Text(text = "Help", style = MaterialTheme.typography.bodyMedium)
        }
        HorizontalDivider()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.HelpOutline,
                contentDescription = "FAQ"
            )
            Text(text = "FAQ", style = MaterialTheme.typography.bodyMedium)
        }
        HorizontalDivider()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.PrivacyTip,
                contentDescription = "notification settings"
            )
            Text(text = "Privacy Policy", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun CircularPhoto(modifier: Modifier = Modifier, selectedMedia: Uri) {
    val rainbowColorsBrush = remember {
        Brush.sweepGradient(
            listOf(
                Color(0xFF9575CD),
                Color(0xFFBA68C8),
                Color(0xFFE57373),
                Color(0xFFFFB74D),
                Color(0xFFFFF176),
                Color(0xFFAED581),
                Color(0xFF4DD0E1),
                Color(0xFF9575CD)
            )
        )
    }

    AsyncImage(
        model = selectedMedia,
        placeholder = painterResource(id = R.drawable.ic_launcher_foreground),
        contentDescription = "profile pic",
        contentScale = ContentScale.Crop,
        fallback = painterResource(id = R.drawable.ic_launcher_foreground),
        error = painterResource(id = R.drawable.ic_launcher_foreground),
        modifier = Modifier
            .padding(top = 8.dp)
            .size(150.dp)
            .clip(CircleShape)
            .border(brush = rainbowColorsBrush, width = 4.dp, shape = CircleShape)
    )
}

//        topBar = {
//            TopAppBar(
//                colors = TopAppBarDefaults.topAppBarColors(
//                    containerColor = Color.Transparent
//                ),
//                title = {
//                    Row(
//                        verticalAlignment = Alignment.CenterVertically,
//                        modifier = Modifier.fillMaxWidth()
//                    ) {
//                        Image(
//                            painter = painterResource(id = R.drawable.desk),
//                            contentDescription = "profile pic",
//                            contentScale = ContentScale.Crop,
//                            modifier = Modifier
//                                .size(50.dp)
//                                .clip(CircleShape)
//                        )
//                        Column(
//                            modifier = Modifier.padding(8.dp)
//                        ) {
//                            Text(
//                                text = "Name",
//                                style = MaterialTheme.typography.headlineMedium,
//                            )
//                            Text(
//                                text = "last seen recently",
//                                style = MaterialTheme.typography.labelMedium,
//                            )
//                        }
//                    }
//                }, actions = {
//                    IconButton(onClick = { /*TODO*/ }) {
//                        Icon(
//                            imageVector = Icons.Default.Edit, contentDescription = null
//                        )
//                    }
//                    IconButton(onClick = { /*TODO*/ }) {
//                        Icon(
//                            imageVector = Icons.Default.MoreVert, contentDescription = null
//                        )
//                    }
//                }, navigationIcon = {
//                    IconButton(onClick = { /*TODO*/ }) {
//                        Icon(
//                            imageVector = Icons.AutoMirrored.Default.ArrowBackIos,
//                            contentDescription = null
//                        )
//                    }
//                })
//        },
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileScreenPreview(modifier: Modifier = Modifier) {
    ProfileScreen(
        navHostController = rememberNavController(),
        viewModel = hiltViewModel()
    )
}
