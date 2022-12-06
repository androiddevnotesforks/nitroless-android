package com.paraskcd.nitroless.components

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.paraskcd.nitroless.model.Repo
import com.paraskcd.nitroless.ui.theme.*
import com.paraskcd.nitroless.utils.NetworkImage
import com.paraskcd.nitroless.viewmodel.RepoViewModel

@Composable
fun Drawer(isHomeActive: Boolean, isDrawerActive: Boolean, openDrawer: (Boolean) -> Unit, openCommunityRepos: (Boolean) -> Unit, viewModel: RepoViewModel, makeHomeActive: (Boolean) -> Unit) {
    val density = LocalDensity.current
    val repos = viewModel.repos.observeAsState()
    val loadingRepos = viewModel.loadingRepos.observeAsState()

    val refreshCount by remember {
        mutableStateOf(1)
    }

    LaunchedEffect(key1 = refreshCount) {
        viewModel.getReposData()
    }

    AnimatedVisibility(
        visible = isDrawerActive,
        enter =
        slideInHorizontally {
            with(density) { -40.dp.roundToPx() }
        } + expandHorizontally(expandFrom = Alignment.Start) + fadeIn(initialAlpha = 0.3f),
        exit = slideOutHorizontally() + shrinkHorizontally() + fadeOut()
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxHeight()
                .background(color = MaterialTheme.colors.primaryVariant)
                .width(72.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            item {
                IconButton(
                    onClick = { openDrawer(false) },
                    modifier = Modifier
                        .padding(bottom = 5.dp)
                        .size(50.dp)
                        .clip(CircleShape)
                        .offset(y = 5.dp)
                ) {
                    Icon(
                        Icons.Rounded.Close,
                        contentDescription = "Close Button"
                    )
                }
            }
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.width(3.dp)) {
                        AnimatedVisibility(
                            visible = isHomeActive,
                            enter = expandVertically(expandFrom = Alignment.CenterVertically) + fadeIn(initialAlpha = 0.3f),
                            exit = fadeOut()
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(32.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(MaterialTheme.colors.onPrimary)
                            )
                        }
                    }
                    IconButton(onClick = {
                        makeHomeActive(true)
                        viewModel.deselectAllRepos()
                        openDrawer(false)
                    }) {
                        AnimatedVisibility(
                            visible = isHomeActive,
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            Image(
                                painter = painterResource(id = com.paraskcd.nitroless.R.drawable.app_icon),
                                contentDescription = "",
                                modifier = Modifier
                                    .padding(10.dp)
                                    .clip(RoundedCornerShape(8.dp))
                            )
                        }
                        AnimatedVisibility(
                            visible = !isHomeActive,
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            Image(
                                painter = painterResource(id = com.paraskcd.nitroless.R.drawable.app_icon),
                                contentDescription = "",
                                modifier = Modifier
                                    .padding(10.dp)
                                    .clip(CircleShape)
                            )
                        }
                    }
                }
            }
            if (loadingRepos.value == true) {
                item {
                    Divider(
                        modifier = Modifier
                            .padding(horizontal = 15.dp)
                            .offset(x = 2.dp)
                    )
                }
                item {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .padding(horizontal = 15.dp)
                            .offset(x = 2.dp),
                        color = MaterialTheme.colors.onPrimary
                    )
                }
            } else {
                if (repos != null) {
                    item {
                        Divider(
                            modifier = Modifier
                                .padding(horizontal = 15.dp)
                                .offset(x = 2.dp)
                        )
                    }
                    items(repos.value!!) { repo ->
                        DrawerItem(
                            onClick = {
                                openDrawer(false)
                                viewModel.deselectAllRepos()
                                viewModel.selectRepo(repo)
                                makeHomeActive(false)
                            },
                            repo = repo
                        )
                    }
                }
            }
            item {
                Divider(
                    modifier = Modifier
                        .padding(horizontal = 15.dp)
                        .offset(x = 2.dp)
                )
            }
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        modifier = Modifier
                            .width(5.dp)
                            .background(Color.Transparent)
                    )
                    IconButton(
                        onClick = {
                            openDrawer(false)
                            openCommunityRepos(true)
                        },
                        modifier = Modifier
                            .padding(vertical = 10.dp)
                            .clip(CircleShape)
                            .background(AccentColor)
                            .size(50.dp)
                    ) {
                        Image(
                            painter = painterResource(id = com.paraskcd.nitroless.R.drawable.globe2),
                            contentDescription = "",
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }
            }
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        modifier = Modifier
                            .width(5.dp)
                            .background(Color.Transparent)
                    )
                    IconButton(
                        onClick = { /*TODO*/ },
                        modifier = Modifier
                            .padding(bottom = 10.dp)
                            .clip(CircleShape)
                            .background(
                                Success
                            )
                            .size(50.dp)
                    ) {
                        Icon(
                            Icons.Rounded.AddCircle,
                            contentDescription = "",
                            modifier = Modifier.padding(5.dp),
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DrawerItem(modifier: Modifier = Modifier, onClick: () -> Unit, repo: Repo) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.width(3.dp)) {
            AnimatedVisibility(
                visible = repo.selected,
                enter = expandVertically(expandFrom = Alignment.CenterVertically) + fadeIn(initialAlpha = 0.3f),
                exit = fadeOut(),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(32.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colors.onPrimary)
                )
            }
        }
        IconButton(onClick = {
            onClick()
        }) {
            AnimatedVisibility(
                visible = repo.selected,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                NetworkImage(imageURL = repo.url + repo.icon, imageDescription = repo.name, size = 50.dp, shape = RoundedCornerShape(8.dp))
            }
            AnimatedVisibility(
                visible = !repo.selected,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                NetworkImage(imageURL = repo.url + repo.icon, imageDescription = repo.name, size = 50.dp, shape = CircleShape)
            }
        }
    }
}