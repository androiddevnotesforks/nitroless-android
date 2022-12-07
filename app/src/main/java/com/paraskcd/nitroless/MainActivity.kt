package com.paraskcd.nitroless

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.*
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.*
import com.paraskcd.nitroless.components.AddRepoPromptDialog
import com.paraskcd.nitroless.components.CommunityReposUI
import com.paraskcd.nitroless.components.DeleteRepoPromptDialog
import com.paraskcd.nitroless.components.Drawer
import com.paraskcd.nitroless.model.FrequentlyUsedEmotesTable
import com.paraskcd.nitroless.model.Repo
import com.paraskcd.nitroless.model.RepoTable
import com.paraskcd.nitroless.screens.Repo
import com.paraskcd.nitroless.ui.theme.*
import com.paraskcd.nitroless.viewmodel.RepoViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var repoToAdd: String by remember {
                mutableStateOf("")
            }
            var isAddRepoActive: Boolean by remember {
                mutableStateOf(false)
            }
            var isDeleteRepoActive: Boolean by remember {
                mutableStateOf(false)
            }

            var isHomeActive by remember { mutableStateOf(true) }
            var isDrawerActive by remember { mutableStateOf(false) }
            var isCommunityReposActive by remember { mutableStateOf(false) }
            val animateDrawer: Dp by animateDpAsState(
                if (isDrawerActive) 72.dp else 0.dp
            )
            val viewModel: RepoViewModel = hiltViewModel()

            val frequentlyUsedEmotes = viewModel.frequentlyUsedEmotes.collectAsState().value
            val selectedRepo = viewModel.selectedRepo.observeAsState().value
            val repos = viewModel.repos.observeAsState().value
            val loadingRepos = viewModel.loadingRepos.observeAsState().value

            var refreshCount by remember {
                mutableStateOf(1)
            }

            LaunchedEffect(key1 = refreshCount) {
                viewModel.getReposData()
            }

            val intent: Intent? = getIntent()

            LaunchedEffect(key1 = intent) {
                if (Intent.ACTION_VIEW == intent?.action) {
                    val uri: Uri? = intent.data
                    var url: String? = uri?.getQueryParameter("url")
                    if (url != null) {
                        if (url[url.length - 1] != '/') {
                            url += '/'
                        }
                        repoToAdd = url
                        isAddRepoActive = true
                    }
                }
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
                            makeHomeActive = { isHomeActive = it },
                            frequentlyUsedEmotes = frequentlyUsedEmotes,
                            refresh = { refreshCount ++ },
                            selectedRepo = selectedRepo,
                            repoEmptyFlag = repos != null && repos.isEmpty(),
                            showDeleteRepoDialog = { isDeleteRepoActive = it }
                        )
                        Drawer(
                            isHomeActive = isHomeActive,
                            isDrawerActive = isDrawerActive,
                            openDrawer = { isDrawerActive = it },
                            openCommunityRepos = { isCommunityReposActive = it },
                            viewModel = viewModel,
                            makeHomeActive = { isHomeActive = it },
                            refresh = { refreshCount ++ },
                            repos = repos,
                            loadingRepos = loadingRepos,
                            showAddRepoDialog = { isAddRepoActive = it }
                        )
                        CommunityReposUI(
                            isCommunityReposActive = isCommunityReposActive,
                            viewModel = viewModel,
                            openCommunityRepos = { isCommunityReposActive = it }
                        )
                        AddRepoPromptDialog(
                            show = isAddRepoActive,
                            addRepo = { repoToAdd = it },
                            repoToAdd = repoToAdd,
                            addRepoButtonOnClick = {
                                viewModel.addRepo(RepoTable(repoURL = repoToAdd))
                                isAddRepoActive = false
                            },
                            cancelButtonOnClick = {
                                repoToAdd = ""
                                isAddRepoActive = false
                            }
                        )
                        if(selectedRepo != null) {
                            DeleteRepoPromptDialog(
                                show = isDeleteRepoActive,
                                deleteRepoOnClick = {
                                    isDeleteRepoActive = false
                                    viewModel.deleteRepo(repo = RepoTable(id = selectedRepo.id!!, repoURL = selectedRepo.url!!))
                                    refreshCount++
                                    viewModel.deselectAllRepos()
                                    isHomeActive = true
                                },
                                cancelButtonOnClick = {
                                    isDeleteRepoActive = false
                                },
                                repoName = selectedRepo.name
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Navigation(viewModel: RepoViewModel, openDrawer: (Boolean) -> Unit, animateDrawer: Dp, isDrawerActive: Boolean, isHomeActive: Boolean, makeHomeActive: (Boolean) -> Unit, frequentlyUsedEmotes: List<FrequentlyUsedEmotesTable>, refresh: () -> Unit, selectedRepo: Repo?, repoEmptyFlag: Boolean, showDeleteRepoDialog: (Boolean) -> Unit) {
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
                        viewModel = viewModel,
                        frequentlyUsedEmotes = frequentlyUsedEmotes,
                        repoEmptyFlag = repoEmptyFlag
                    )
                } else {
                    Repo(
                        openDrawer = { openDrawer(true) },
                        closeDrawer = { openDrawer(false) },
                        viewModel = viewModel,
                        animateDrawer = animateDrawer,
                        closeRepo = { makeHomeActive(true) },
                        selectedRepo = selectedRepo,
                        refresh = { refresh() },
                        showDeleteRepoDialog = { showDeleteRepoDialog(true) }
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