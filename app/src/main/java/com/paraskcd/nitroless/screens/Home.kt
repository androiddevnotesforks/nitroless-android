package com.paraskcd.nitroless

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.*
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.*
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.*
import androidx.navigation.*
import com.paraskcd.nitroless.components.*
import com.paraskcd.nitroless.enums.RepoPage
import com.paraskcd.nitroless.model.FavouriteEmotesTable
import com.paraskcd.nitroless.model.FavouriteStickersTable
import com.paraskcd.nitroless.model.FrequentlyUsedEmotesTable
import com.paraskcd.nitroless.model.FrequentlyUsedStickersTable
import com.paraskcd.nitroless.utils.NetworkImage
import com.paraskcd.nitroless.viewmodel.RepoViewModel
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@Composable
fun Home( openDrawer: () -> Unit, closeDrawer: () -> Unit, navController: NavHostController, animateDrawer: Dp, isDrawerActive: Boolean, viewModel: RepoViewModel, frequentlyUsedEmotes: List<FrequentlyUsedEmotesTable>, repoEmptyFlag: Boolean, favouriteEmotes: List<FavouriteEmotesTable>, frequentlyUsedStickers: List<FrequentlyUsedStickersTable>, favouriteStickers: List<FavouriteStickersTable>, repoMenu: Int, onClickRepoMenu: (Int) -> Unit, isCommunityReposActive: Boolean ) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val activity = (LocalContext.current as? Activity)

    BackHandler(enabled = !isCommunityReposActive) {
        if (repoMenu == RepoPage.STICKERS.value) {
            onClickRepoMenu(RepoPage.EMOTES.value)
        } else {
            activity?.finish()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.primary)
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
        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colors.background),
            columns =
                if (repoMenu == RepoPage.EMOTES.value) {
                    GridCells.Fixed(5)
                } else {
                    GridCells.Fixed(4)
                }
        ) {
            if (favouriteStickers.isNotEmpty() || frequentlyUsedStickers.isNotEmpty()) {
                item(span = { GridItemSpan(5) }) {
                    Picker(repoMenu = repoMenu, onClickRepoMenu = { value -> onClickRepoMenu(value) })
                }
            }
            if (repoMenu == RepoPage.EMOTES.value && favouriteEmotes.isNotEmpty()) {
                item(span = { GridItemSpan(5) }) {
                    Container {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(18.dp)
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
                        }
                    }
                }
                items(favouriteEmotes) { emote ->
                    Container {
                        Column(
                            modifier = Modifier
                                .width(60.dp)
                                .height(60.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
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
                item(span = { GridItemSpan(5) }) {
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
            if (repoMenu == RepoPage.STICKERS.value && favouriteStickers.isNotEmpty()) {
                item(span = { GridItemSpan(5) }) {
                    Container {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(18.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Filled.Star, contentDescription = "")
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    "Favourite Stickers",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight(700)
                                )
                            }
                        }
                    }
                }
                items(favouriteStickers) { sticker ->
                    Container {
                        Column(
                            modifier = Modifier
                                .width(90.dp)
                                .height(90.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            IconButton(
                                onClick = {
                                    val stickerURL = sticker.stickerURL
                                    viewModel.addFrequentlyUsedSticker(
                                        sticker = FrequentlyUsedStickersTable(stickerURL = stickerURL)
                                    )
                                    clipboardManager.setText(AnnotatedString(stickerURL))
                                    Toast.makeText(
                                        context,
                                        "Copied Sticker",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            ) {
                                NetworkImage(
                                    imageURL = sticker.stickerURL,
                                    imageDescription = sticker.stickerURL,
                                    size = 72.dp,
                                    shape = RoundedCornerShape(10.dp)
                                )
                            }
                        }
                    }
                }
                item(span = { GridItemSpan(5) }) {
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
            if (repoMenu == RepoPage.EMOTES.value) {
                item(span = { GridItemSpan(5) }) {
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
                                if (frequentlyUsedEmotes.isEmpty()) {
                                    Text(text = "Start using Nitroless to show your frequently used emotes here.")
                                }
                            }
                        }
                    }
                }
                if (!repoEmptyFlag && frequentlyUsedEmotes.isNotEmpty()) {
                    items(frequentlyUsedEmotes.reversed()) { emote ->
                        Container {
                            Column(
                                modifier = Modifier
                                    .width(60.dp)
                                    .height(60.dp),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
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
                item(span = { GridItemSpan(5) }) {
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
            if (repoMenu == RepoPage.STICKERS.value && frequentlyUsedStickers.isNotEmpty()) {
                item(span = { GridItemSpan(5) }) {
                    Container {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(18.dp)
                        ){
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.history_icon),
                                    contentDescription = ""
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    "Frequently Used Stickers",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight(700)
                                )
                            }
                        }
                    }
                }
                items(frequentlyUsedStickers.reversed()) { sticker ->
                    Container {
                        Column(
                            modifier = Modifier
                                .width(90.dp)
                                .height(90.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            IconButton(
                                onClick = {
                                    val stickerURL = sticker.stickerURL
                                    viewModel.addFrequentlyUsedSticker(
                                        sticker = FrequentlyUsedStickersTable(stickerURL = stickerURL)
                                    )
                                    clipboardManager.setText(AnnotatedString(stickerURL))
                                    Toast.makeText(
                                        context,
                                        "Copied Sticker",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            ) {
                                NetworkImage(
                                    imageURL = sticker.stickerURL,
                                    imageDescription = sticker.stickerURL,
                                    size = 72.dp,
                                    shape = RoundedCornerShape(10.dp)
                                )
                            }
                        }
                    }
                }
                item(span = { GridItemSpan(5) }) {
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}
