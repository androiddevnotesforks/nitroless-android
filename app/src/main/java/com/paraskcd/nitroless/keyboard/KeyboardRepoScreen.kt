package com.paraskcd.nitroless.keyboard

import android.content.Context
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.paraskcd.nitroless.R
import com.paraskcd.nitroless.model.FrequentlyUsedEmotesTable
import com.paraskcd.nitroless.model.Repo
import com.paraskcd.nitroless.ui.theme.*
import com.paraskcd.nitroless.viewmodel.RepoViewModel

@Composable
fun KeyboardRepoScreen(context: Context, selectedRepo: Repo?, viewModel: RepoViewModel, repos: List<Repo>?, textColor: Color, bgPrimaryColor: Color, bgSecondaryColor: Color, bgTertiaryColor: Color, backspace_icon: Int, hideRepositories: Boolean, hideFavouriteEmotes: Boolean) {
    val clipboardManager = LocalClipboardManager.current

    Column(
        modifier = Modifier
            .background(bgPrimaryColor)
            .fillMaxWidth()
            .height(420.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        if (repos != null && selectedRepo != null) {
            Column(modifier = Modifier.height(360.dp)) {
                LazyVerticalGrid(modifier = Modifier.fillMaxHeight(), columns = GridCells.Fixed(5)) {
                    if (selectedRepo.stickers != null) {
                        item(span = { GridItemSpan(5) }) {
                            TabRow(
                                selectedTabIndex = 0,
                                backgroundColor = bgTertiaryColor,
                                modifier = Modifier
                                    .padding(vertical = 10.dp, horizontal = 40.dp)
                                    .shadow(elevation = 10.dp, shape = CircleShape)
                                    .clip(CircleShape),
                                indicator = { tabPositions: List<TabPosition> ->
                                    Box {}
                                },
                                divider = @Composable {
                                    Spacer(modifier = Modifier)
                                }
                            ) {
                                val list = listOf("Emotes", "Stickers")
                                list.forEachIndexed { index, text ->
                                    val selected = 0 == index
                                    Tab(
                                        selected = selected,
                                        onClick = {
                                            Log.d("DEBUGGG", index.toString())
                                            if (index == 0) {
                                                (context as IMEService).setInputView(ComposeKeyboardRepoView(context))
                                            } else {
                                                (context as IMEService).setInputView(ComposeKeyboardStickersRepoView(context))
                                            }
                                        },
                                        modifier =
                                        if (selected) {
                                            Modifier
                                                .clip(CircleShape)
                                                .background(AccentColor)
                                        } else {
                                            Modifier
                                                .clip(CircleShape)
                                                .background(bgTertiaryColor)
                                        },
                                        text = {
                                            Text(
                                                text = text,
                                                color =
                                                if (selected) {
                                                    Color.White
                                                } else {
                                                    textColor
                                                }
                                            )
                                        }
                                    )
                                }
                            }
                        }
                    } else {
                        item(span = { GridItemSpan(5) }) {
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                    }
                    if (selectedRepo.favouriteEmotes != null && selectedRepo.favouriteEmotes!!.isNotEmpty() && !hideFavouriteEmotes) {
                        item(span = { GridItemSpan(7) }) {
                            Card(
                                backgroundColor = bgSecondaryColor,
                                contentColor = textColor,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 10.dp)
                                    .padding(bottom = 5.dp)
                                    .padding(horizontal = 10.dp),
                                border = BorderStroke(
                                    width = 1.dp,
                                    color = bgTertiaryColor.copy(alpha = 0.1F)
                                ),
                                shape = RoundedCornerShape(20.dp),
                                elevation = 10.dp
                            ) {
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
                            Card(
                                backgroundColor = bgSecondaryColor,
                                contentColor = textColor,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 10.dp)
                                    .padding(bottom = 5.dp)
                                    .padding(horizontal = 10.dp),
                                border = BorderStroke(
                                    width = 1.dp,
                                    color = bgTertiaryColor.copy(alpha = 0.1F)
                                ),
                                shape = RoundedCornerShape(20.dp),
                                elevation = 10.dp
                            ) {
                                Column(
                                    modifier = Modifier
                                        .width(50.dp)
                                        .height(50.dp),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    val emoteURL = emote.emoteURL
                                    IconButton(
                                        onClick = {
                                            viewModel.addFrequentlyUsedEmote(
                                                emote = FrequentlyUsedEmotesTable(emoteURL = emoteURL)
                                            )
                                            clipboardManager.setText(AnnotatedString(emoteURL))
                                            (context as IMEService).currentInputConnection.commitText(emoteURL, emoteURL.length)
                                            context.setInputView(ComposeKeyboardView(context))
                                        }
                                    ) {
                                        NetworkKeyboardImage(
                                            imageURL = emoteURL,
                                            imageDescription = emoteURL,
                                            size = 32.dp,
                                            shape = RoundedCornerShape(10.dp),
                                            bgSecondaryColor = bgSecondaryColor
                                        )
                                    }
                                }
                            }
                        }
                        item(span = { GridItemSpan(5) }) {
                            Spacer(modifier = Modifier.height(20.dp))
                        }
                    }
                    item(span = { GridItemSpan(5) }) {
                        Card(
                            backgroundColor = bgSecondaryColor,
                            contentColor = textColor,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp)
                                .padding(bottom = 5.dp)
                                .padding(horizontal = 10.dp),
                            border = BorderStroke(
                                width = 1.dp,
                                color = bgTertiaryColor.copy(alpha = 0.1F)
                            ),
                            shape = RoundedCornerShape(20.dp),
                            elevation = 10.dp
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(18.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    NetworkKeyboardImageWOPadding(
                                        imageURL = selectedRepo.url + selectedRepo.icon,
                                        imageDescription = "",
                                        size = 25.dp,
                                        shape = CircleShape,
                                        bgSecondaryColor = bgSecondaryColor
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        selectedRepo.name,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight(700)
                                    )
                                }
                            }
                        }
                    }
                    items(selectedRepo.emotes) { emote ->
                        val emoteURL = selectedRepo.url + selectedRepo.path + "/" + emote.name + "." + emote.type
                        Card(
                            backgroundColor = bgSecondaryColor,
                            contentColor = textColor,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp)
                                .padding(bottom = 5.dp)
                                .padding(horizontal = 10.dp),
                            border = BorderStroke(
                                width = 1.dp,
                                color = bgTertiaryColor.copy(alpha = 0.1F)
                            ),
                            shape = RoundedCornerShape(20.dp),
                            elevation = 10.dp
                        ) {
                            Column(
                                modifier = Modifier
                                    .width(50.dp)
                                    .height(50.dp),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                IconButton(
                                    onClick = {
                                        viewModel.addFrequentlyUsedEmote(
                                            emote = FrequentlyUsedEmotesTable(emoteURL = emoteURL)
                                        )
                                        clipboardManager.setText(AnnotatedString(emoteURL))
                                        (context as IMEService).currentInputConnection.commitText(emoteURL, emoteURL.length)
                                        context.setInputView(ComposeKeyboardView(context))
                                    }
                                ) {
                                    NetworkKeyboardImage(
                                        imageURL = emoteURL,
                                        imageDescription = emoteURL,
                                        size = 32.dp,
                                        shape = RoundedCornerShape(10.dp),
                                        bgSecondaryColor = bgSecondaryColor
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
            BottomBar(
                context = context,
                repos = repos,
                viewModel = viewModel,
                bgSecondaryColor = bgSecondaryColor,
                backspace_icon = backspace_icon,
                hideRepositories = hideRepositories
            )
        } else {
            Card(
                backgroundColor = bgSecondaryColor,
                contentColor = textColor,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
                    .padding(bottom = 5.dp)
                    .padding(horizontal = 10.dp),
                border = BorderStroke(
                    width = 1.dp,
                    color = bgTertiaryColor.copy(alpha = 0.1F)
                ),
                shape = RoundedCornerShape(20.dp),
                elevation = 10.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    Text("Welcome to Nitroless", fontSize = 18.sp, fontWeight = FontWeight(700))
                    Spacer(modifier = Modifier.height(5.dp))
                    Text("Things might have gotten weird, and you now have this Error. For some reason the Keyboard couldn't load the Repos, please try again later.")
                }
            }
        }
    }
}