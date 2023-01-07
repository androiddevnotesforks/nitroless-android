package com.paraskcd.nitroless.keyboard

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.imageLoader
import com.paraskcd.nitroless.model.FrequentlyUsedStickersTable
import com.paraskcd.nitroless.model.Repo
import com.paraskcd.nitroless.ui.theme.AccentColor
import com.paraskcd.nitroless.viewmodel.RepoViewModel

@Composable
fun KeyboardStickersRepoScreen(context: Context, selectedRepo: Repo, repos: List<Repo>?, viewModel: RepoViewModel, textColor: Color, bgPrimaryColor: Color, bgSecondaryColor: Color, bgTertiaryColor: Color, backspace_icon: Int, hideRepositories: Boolean, hideFavouriteEmotes: Boolean) {
    Column(
        modifier = Modifier
            .background(bgPrimaryColor)
            .fillMaxWidth()
            .height(420.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        if (repos != null && selectedRepo != null) {
            Column(modifier = Modifier.height(360.dp)) {
                LazyVerticalGrid(
                    modifier = Modifier.fillMaxHeight(),
                    columns = GridCells.Fixed(3)
                ) {
                    item(span = { GridItemSpan(7) }) {
                        TabRow(
                            selectedTabIndex = 1,
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
                                val selected = 1 == index
                                Tab(
                                    selected = selected,
                                    onClick = {
                                        if (index == 1) {
                                            (context as IMEService).setInputView(ComposeKeyboardStickersRepoView(context))
                                        } else {
                                            (context as IMEService).setInputView(ComposeKeyboardRepoView(context))
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
                    if (selectedRepo.favouriteStickers != null && selectedRepo.favouriteStickers!!.isNotEmpty() && !hideFavouriteEmotes) {
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
                                            "Favourite Stickers",
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight(700)
                                        )
                                    }
                                }
                            }
                        }
                        items(selectedRepo.favouriteStickers!!) { sticker ->
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
                                        .width(72.dp)
                                        .height(120.dp),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    val stickerURL = sticker.stickerURL

                                    IconButton(
                                        onClick = {
                                            viewModel.addFrequentlyUsedSticker(
                                                sticker = FrequentlyUsedStickersTable(stickerURL = stickerURL)
                                            )
                                            context.imageLoader.diskCache?.get(stickerURL)?.use { snapshot ->
                                                val imageFile = snapshot.data.toFile()
                                                (context as IMEService).doCommitContent("Nitroless Emote", "image/webp", imageFile, format = Bitmap.CompressFormat.WEBP)
                                            }
                                        }
                                    ) {
                                        NetworkKeyboardImage(
                                            imageURL = stickerURL,
                                            imageDescription = stickerURL,
                                            size = 90.dp,
                                            shape = RoundedCornerShape(10.dp),
                                            bgSecondaryColor = bgSecondaryColor
                                        )
                                    }
                                }
                            }
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
                    items(selectedRepo.stickers!!) { sticker ->
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
                                    .width(72.dp)
                                    .height(120.dp),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                val stickerURL = selectedRepo.url + selectedRepo.stickerPath + "/" + sticker.name + "." + sticker.type

                                IconButton(
                                    onClick = {
                                        viewModel.addFrequentlyUsedSticker(
                                            sticker = FrequentlyUsedStickersTable(stickerURL = stickerURL)
                                        )
                                        context.imageLoader.diskCache?.get(stickerURL)?.use { snapshot ->
                                            val imageFile = snapshot.data.toFile()
                                            (context as IMEService).doCommitContent("Nitroless Emote", "image/webp", imageFile, format = Bitmap.CompressFormat.WEBP)
                                        }
                                    }
                                ) {
                                    NetworkKeyboardImage(
                                        imageURL = stickerURL,
                                        imageDescription = stickerURL,
                                        size = 90.dp,
                                        shape = RoundedCornerShape(10.dp),
                                        bgSecondaryColor = bgSecondaryColor
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
        BottomStickerBar(
            context = context,
            repos = repos,
            viewModel = viewModel,
            bgSecondaryColor = bgSecondaryColor,
            backspace_icon = backspace_icon,
            hideRepositories = hideRepositories
        )
    }
}