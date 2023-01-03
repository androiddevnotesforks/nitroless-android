package com.paraskcd.nitroless.screens

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.paraskcd.nitroless.R
import com.paraskcd.nitroless.components.Container
import com.paraskcd.nitroless.components.DarkContainerPill
import com.paraskcd.nitroless.components.Picker
import com.paraskcd.nitroless.components.TopBarRepo
import com.paraskcd.nitroless.enums.RepoPage
import com.paraskcd.nitroless.model.*
import com.paraskcd.nitroless.ui.theme.AccentColor
import com.paraskcd.nitroless.utils.NetworkImage
import com.paraskcd.nitroless.viewmodel.RepoViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Repo(isDrawerActive: Boolean, animateDrawer: Dp, openDrawer: () -> Unit, closeDrawer: () -> Unit, viewModel: RepoViewModel, closeRepo: () -> Unit, selectedRepo: Repo?, refresh: () -> Unit, showDeleteRepoDialog: () -> Unit, showContextMenuPromptDialog: () -> Unit, repoMenu: Int, onClickRepoMenu: (Int) -> Unit, openCommunityRepos: (Boolean) -> Unit, isCommunityReposActive: Boolean) {
    BackHandler {
        if (isCommunityReposActive) {
            openCommunityRepos(false)
        } else {
            if (repoMenu == RepoPage.STICKERS.value) {
                onClickRepoMenu(RepoPage.EMOTES.value)
            } else {
                if (isDrawerActive) {
                    closeRepo()
                    viewModel.deselectAllRepos()
                    closeDrawer()
                } else {
                    openDrawer()
                }
            }
        }
    }
    val clipboardManager = LocalClipboardManager.current
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, "Check out this awesome Nitroless Repo - \n${selectedRepo!!.name}\n${selectedRepo.url}")
        putExtra(Intent.EXTRA_TITLE, selectedRepo.name)
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, null)
    val context = LocalContext.current

    if (selectedRepo != null) {
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
            TopBarRepo(
                titleName = null,
                buttonIcon = Icons.Filled.Menu,
                onNavButtonClicked = { openDrawer() },
                onShareButtonClicked = { context.startActivity(shareIntent) },
                onRepoDeleteButtonClicked = { showDeleteRepoDialog() }
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
                item(span = { GridItemSpan(5) }) {
                    DarkContainerPill {
                        Column {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    NetworkImage(
                                        imageURL = selectedRepo.url + selectedRepo.icon,
                                        imageDescription = selectedRepo.name,
                                        size = 50.dp,
                                        shape = CircleShape
                                    )
                                    Column(modifier = Modifier.padding(horizontal = 10.dp)) {
                                        Text(text = selectedRepo.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                        if (selectedRepo.author != null) {
                                            Text(text = "By ${selectedRepo.author}", fontWeight = FontWeight.Light, fontSize = 12.sp)
                                        }
                                    }
                                }
                                if (repoMenu == RepoPage.STICKERS.value) {
                                    Text(text = "${selectedRepo.stickers?.size} Stickers", fontWeight = FontWeight.Light, fontSize = 12.sp, modifier = Modifier.padding(10.dp))
                                } else {
                                    Text(text = "${selectedRepo.emotes.size} Emotes", fontWeight = FontWeight.Light, fontSize = 12.sp, modifier = Modifier.padding(10.dp))
                                }
                            }
                        }
                    }
                }
                if (selectedRepo.stickers != null) {
                    item(span = { GridItemSpan(5) }) {
                        Picker(repoMenu = repoMenu, onClickRepoMenu = { value -> onClickRepoMenu(value) })
                    }
                }
                if (repoMenu == RepoPage.EMOTES.value) {
                    if (selectedRepo.favouriteEmotes != null && selectedRepo.favouriteEmotes!!.isNotEmpty()) {
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
                        items(selectedRepo.favouriteEmotes!!) { emote ->
                            val emoteURL =
                                emote.emoteURL
                            Container {
                                Column(
                                    modifier = Modifier
                                        .width(60.dp)
                                        .height(60.dp),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Column(
                                        modifier = Modifier.combinedClickable(
                                            enabled = true,
                                            onLongClick = {
                                                viewModel.selectEmote(FavouriteEmotesTable(repoURL = selectedRepo.url!!, emoteURL = emoteURL))
                                                showContextMenuPromptDialog()
                                            },
                                            onClick = {
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
                                        )
                                    ) {
                                        NetworkImage(imageURL = emoteURL, size = 48.dp, shape = RoundedCornerShape(10.dp), imageDescription = "")
                                    }
                                }
                            }
                        }
                        item(span = { GridItemSpan(5) }) {
                            Spacer(modifier = Modifier.height(20.dp))
                        }
                        item(span = { GridItemSpan(5) }) {
                            Divider(modifier = Modifier.padding(horizontal = 20.dp))
                        }
                        item(span = { GridItemSpan(5) }) {
                            Spacer(modifier = Modifier.height(20.dp))
                        }
                    }
                    items(selectedRepo.emotes) { emote ->
                        val emoteURL =
                            selectedRepo.url + selectedRepo.path + '/' + emote.name + "." + emote.type
                        Container {
                            Column(
                                modifier = Modifier
                                    .width(60.dp)
                                    .height(60.dp),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ){
                                Column(
                                    modifier = Modifier.combinedClickable(
                                        enabled = true,
                                        onLongClick = {
                                            viewModel.selectEmote(FavouriteEmotesTable(repoURL = selectedRepo.url!!, emoteURL = emoteURL))
                                            showContextMenuPromptDialog()
                                        },
                                        onClick = {
                                            viewModel.addFrequentlyUsedEmote(
                                                emote = FrequentlyUsedEmotesTable(emoteURL = emoteURL)
                                            )
                                            clipboardManager.setText(AnnotatedString(emoteURL))
                                            Toast.makeText(
                                                context,
                                                "Copied Emote - ${emote.name}",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    )
                                ) {
                                    NetworkImage(imageURL = selectedRepo.url + selectedRepo.path + '/' + emote.name + "." + emote.type, imageDescription = emote.name, size = 48.dp, shape = RoundedCornerShape(10.dp))
                                }
                            }
                        }
                    }
                    item(span = { GridItemSpan(5) }) {
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }

                if (repoMenu == RepoPage.STICKERS.value) {
                    if (selectedRepo.favouriteStickers != null && selectedRepo.favouriteStickers!!.isNotEmpty()) {
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
                        items(selectedRepo.favouriteStickers!!) { sticker ->
                            val stickerURL =
                                sticker.stickerURL
                            Container {
                                Column(
                                    modifier = Modifier
                                        .width(90.dp)
                                        .height(90.dp),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Column(
                                        modifier = Modifier.combinedClickable(
                                            enabled = true,
                                            onLongClick = {
                                                viewModel.selectSticker(FavouriteStickersTable(repoURL = selectedRepo.url!!, stickerURL = stickerURL))
                                                showContextMenuPromptDialog()
                                            },
                                            onClick = {
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
                                        )
                                    ) {
                                        NetworkImage(imageURL = stickerURL, size = 72.dp, shape = RoundedCornerShape(10.dp), imageDescription = "")
                                    }
                                }
                            }
                        }
                        item(span = { GridItemSpan(5) }) {
                            Spacer(modifier = Modifier.height(20.dp))
                        }
                        item(span = { GridItemSpan(5) }) {
                            Divider(modifier = Modifier.padding(horizontal = 20.dp))
                        }
                        item(span = { GridItemSpan(5) }) {
                            Spacer(modifier = Modifier.height(20.dp))
                        }
                    }
                    if (selectedRepo.stickers?.isNotEmpty() == true) {
                        items(selectedRepo.stickers) { sticker ->
                            val stickerURL =
                                selectedRepo.url + selectedRepo.stickerPath + '/' + sticker.name + "." + sticker.type
                            Container {
                                Column(
                                    modifier = Modifier
                                        .width(90.dp)
                                        .height(90.dp),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ){
                                    Column(
                                        modifier = Modifier.combinedClickable(
                                            enabled = true,
                                            onLongClick = {
                                                viewModel.selectSticker(FavouriteStickersTable(repoURL = selectedRepo.url!!, stickerURL = stickerURL))
                                                showContextMenuPromptDialog()
                                            },
                                            onClick = {
                                                viewModel.addFrequentlyUsedSticker(
                                                    sticker = FrequentlyUsedStickersTable(stickerURL = stickerURL)
                                                )
                                                clipboardManager.setText(AnnotatedString(stickerURL))
                                                Toast.makeText(
                                                    context,
                                                    "Copied Emote - ${sticker.name}",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        )
                                    ) {
                                        NetworkImage(imageURL = stickerURL, imageDescription = sticker.name, size = 72.dp, shape = RoundedCornerShape(10.dp))
                                    }
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
}