package com.paraskcd.nitroless

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.*
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.*
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.*
import androidx.navigation.*
import com.paraskcd.nitroless.components.*
import com.paraskcd.nitroless.model.FavouriteEmotesTable
import com.paraskcd.nitroless.model.FrequentlyUsedEmotesTable
import com.paraskcd.nitroless.utils.NetworkImage
import com.paraskcd.nitroless.viewmodel.RepoViewModel
import kotlinx.coroutines.flow.asStateFlow

@Composable
fun Home( openDrawer: () -> Unit, closeDrawer: () -> Unit, navController: NavHostController, animateDrawer: Dp, isDrawerActive: Boolean, viewModel: RepoViewModel, frequentlyUsedEmotes: List<FrequentlyUsedEmotesTable>, repoEmptyFlag: Boolean, favouriteEmotes: List<FavouriteEmotesTable> ) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.primary)
    ) {
        TopBar(
            titleName = null,
            buttonIcon = Icons.Filled.Menu,
            onNavButtonClicked = { openDrawer() },
            onInfoButtonClicked = {
                if (isDrawerActive) {
                    closeDrawer()
                }
                navController.navigate("about") {
                    popUpTo("home")
                }
            },
            onSettingsButtonClicked = {
                if (isDrawerActive) {
                    closeDrawer()
                }
                navController.navigate("settings") {
                    popUpTo("home")
                }
            }
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colors.background)
                .draggable(
                    orientation = Orientation.Horizontal,
                    state = rememberDraggableState { delta ->
                        if (delta < 0) {
                            closeDrawer()
                        } else {
                            if (delta > 10) {
                                openDrawer()
                            }
                        }
                    }
                )
        ) {
            if (favouriteEmotes.isNotEmpty()) {
                Container {
                    Column(
                        modifier = Modifier.fillMaxWidth().height(200.dp).padding(18.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Filled.Star, contentDescription = "")
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                "Favourite Emotes",
                                fontSize = 20.sp,
                                fontWeight = FontWeight(700)
                            )
                        }
                        LazyVerticalGrid(columns = GridCells.Adaptive(minSize = 64.dp)) {
                            items(favouriteEmotes) { emote ->
                                IconButton(
                                    onClick = {
                                        val emoteURL = emote.emoteURL
                                        viewModel.addFrequentlyUsedEmote(
                                            emote = FrequentlyUsedEmotesTable(emoteURL = emoteURL)
                                        )
                                        clipboardManager.setText(AnnotatedString(emoteURL))
                                        Toast.makeText(
                                            context,
                                            "Copied Emote",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                ) {
                                    NetworkImage(
                                        imageURL = emote.emoteURL,
                                        imageDescription = emote.emoteURL,
                                        size = 48.dp,
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Container {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    if (repoEmptyFlag) {
                        Text("Welcome to Nitroless", fontSize = 20.sp, fontWeight = FontWeight(700))
                    } else {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.history_icon),
                                contentDescription = ""
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                "Frequently Used Emotes",
                                fontSize = 20.sp,
                                fontWeight = FontWeight(700)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(5.dp))
                    if (repoEmptyFlag) {
                        Text(text = "Start using Nitroless by adding Your Repositories to the app. Tap on the Hamburger menu above to open the Sidebar Drawer and click either on the Globe Button or the Add Button.")
                    } else {
                        if (frequentlyUsedEmotes.isNotEmpty()) {
                            LazyVerticalGrid(columns = GridCells.Adaptive(minSize = 64.dp)) {
                                items(frequentlyUsedEmotes.reversed()) { emote ->
                                    IconButton(
                                        onClick = {
                                            val emoteURL = emote.emoteURL
                                            viewModel.addFrequentlyUsedEmote(
                                                emote = FrequentlyUsedEmotesTable(emoteURL = emoteURL)
                                            )
                                            clipboardManager.setText(AnnotatedString(emoteURL))
                                            Toast.makeText(
                                                context,
                                                "Copied Emote",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    ) {
                                        NetworkImage(
                                            imageURL = emote.emoteURL,
                                            imageDescription = emote.emoteURL,
                                            size = 48.dp,
                                            shape = RoundedCornerShape(10.dp)
                                        )
                                    }
                                }
                            }
                        } else {
                            Text(text = "Start using Nitroless to show your frequently used emotes here.")
                        }
                    }
                }
            }
        }
    }
}
