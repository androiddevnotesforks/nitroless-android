package com.paraskcd.nitroless

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.*
import com.paraskcd.nitroless.components.CommunityReposUI
import com.paraskcd.nitroless.components.Drawer
import com.paraskcd.nitroless.screens.Repo
import com.paraskcd.nitroless.ui.theme.*
import com.paraskcd.nitroless.viewmodel.RepoViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var isHomeActive by remember { mutableStateOf(true) }
            var isDrawerActive by remember { mutableStateOf(false) }
            var isCommunityReposActive by remember { mutableStateOf(false) }
            val animateDrawer: Dp by animateDpAsState(
                if (isDrawerActive) 72.dp else 0.dp
            )
            val viewModel: RepoViewModel = hiltViewModel()
            
            LaunchedEffect(key1 = Unit) {
                viewModel.getReposData()
            }

            NitrolessTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Navigation(
                            viewModel = viewModel,
                            openDrawer = { isDrawerActive = it },
                            animateDrawer = animateDrawer,
                            isDrawerActive = isDrawerActive,
                            isHomeActive = isHomeActive,
                            makeHomeActive = { isHomeActive = it }
                        )
                        Drawer(
                            isHomeActive = isHomeActive,
                            isDrawerActive = isDrawerActive,
                            openDrawer = { isDrawerActive = it },
                            openCommunityRepos = { isCommunityReposActive = it },
                            viewModel = viewModel,
                            makeHomeActive = { isHomeActive = it }
                        )
                        CommunityReposUI(
                            isCommunityReposActive = isCommunityReposActive,
                            viewModel = viewModel,
                            openCommunityRepos = { isCommunityReposActive = it }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Navigation(viewModel: RepoViewModel, openDrawer: (Boolean) -> Unit, animateDrawer: Dp, isDrawerActive: Boolean, isHomeActive: Boolean, makeHomeActive: (Boolean) -> Unit) {
    val navController = rememberNavController()

    NavHost( navController = navController, startDestination = "home" ) {
        composable("home")
            {
                if (isHomeActive) {
                    Home(
                        openDrawer = { openDrawer(true) },
                        closeDrawer = { openDrawer(false) },
                        navController = navController,
                        animateDrawer = animateDrawer,
                        isDrawerActive = isDrawerActive,
                        viewModel = viewModel
                    )
                } else {
                    Repo(
                        openDrawer = { openDrawer(true) },
                        closeDrawer = { openDrawer(false) },
                        viewModel = viewModel,
                        animateDrawer = animateDrawer,
                        closeRepo = { makeHomeActive(true) }
                    )
                }
            }
        composable("about") {
            About(
                navController = navController
            )
        }
    }
}