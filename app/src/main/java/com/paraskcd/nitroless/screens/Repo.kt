package com.paraskcd.nitroless.screens

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
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.paraskcd.nitroless.components.TopBarRepo
import com.paraskcd.nitroless.model.FavouriteEmotesTable
import com.paraskcd.nitroless.model.FrequentlyUsedEmotesTable
import com.paraskcd.nitroless.model.Repo
import com.paraskcd.nitroless.model.RepoTable
import com.paraskcd.nitroless.utils.NetworkImage
import com.paraskcd.nitroless.viewmodel.RepoViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Repo(animateDrawer: Dp, openDrawer: () -> Unit, closeDrawer: () -> Unit, viewModel: RepoViewModel, closeRepo: () -> Unit, selectedRepo: Repo?, refresh: () -> Unit, showDeleteRepoDialog: () -> Unit, showContextMenuPromptDialog: () -> Unit) {
    BackHandler {
        openDrawer()
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
        ) {
            TopBarRepo(
                titleName = null,
                buttonIcon = Icons.Filled.Menu,
                onNavButtonClicked = { openDrawer() },
                onShareButtonClicked = { context.startActivity(shareIntent) },
                onRepoDeleteButtonClicked = { showDeleteRepoDialog() }
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
                DarkContainerPill {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
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
                        Text(text = "${selectedRepo.emotes.size} Emotes", fontWeight = FontWeight.Light, fontSize = 12.sp, modifier = Modifier.padding(10.dp))
                    }
                }
                if (selectedRepo.favouriteEmotes != null && selectedRepo.favouriteEmotes!!.isNotEmpty()) {
                    Container {
                        Column(
                            modifier = Modifier.fillMaxWidth().height(200.dp).padding(10.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Filled.Star, "")
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    "Favourite Emotes",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight(700)
                                )
                            }
                            LazyVerticalGrid(columns = GridCells.Adaptive(minSize = 64.dp)) {
                                items(selectedRepo.favouriteEmotes!!) { emote ->
                                    val emoteURL =
                                        emote.emoteURL
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
                    }
                }
                Container {
                    LazyVerticalGrid(columns = GridCells.Adaptive(minSize = 64.dp)) {
                        items(selectedRepo.emotes) { emote ->
                            val emoteURL =
                                selectedRepo.url + selectedRepo.path + '/' + emote.name + "." + emote.type
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
            }
        }
    }
}