package com.paraskcd.nitroless.keyboard

import android.content.Context
import android.graphics.Bitmap
import android.view.KeyEvent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.imageLoader
import com.paraskcd.nitroless.R
import com.paraskcd.nitroless.model.FavouriteStickersTable
import com.paraskcd.nitroless.model.FrequentlyUsedStickersTable
import com.paraskcd.nitroless.model.Repo
import com.paraskcd.nitroless.ui.theme.AccentColor
import com.paraskcd.nitroless.viewmodel.RepoViewModel

@Composable
fun KeyboardStickerScreen(context: Context, repos: List<Repo>?, viewModel: RepoViewModel, textColor: Color, bgPrimaryColor: Color, bgSecondaryColor: Color, bgTertiaryColor: Color, history_icon: Int, backspace_icon: Int, hideRepositories: Boolean, hideFrequentlyUsedEmotes: Boolean, hideFavouriteEmotes: Boolean, frequentlyUsedStickers: List<FrequentlyUsedStickersTable>, favouriteStickers: List<FavouriteStickersTable>) {
    Column(
        modifier = Modifier
            .background(bgPrimaryColor)
            .fillMaxWidth()
            .height(420.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        if (repos != null) {
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
                                            (context as IMEService).setInputView(ComposeKeyboardStickersView(context))
                                        } else {
                                            (context as IMEService).setInputView(ComposeKeyboardView(context))
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
                    if (!hideFavouriteEmotes && favouriteStickers.isNotEmpty()) {
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
                        items(favouriteStickers) { sticker ->
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
                                                context.setInputView(ComposeKeyboardStickersView(context))
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
                        item(span = { GridItemSpan(5) }) {
                            Spacer(modifier = Modifier.height(20.dp))
                        }
                    }
                    if (!hideFrequentlyUsedEmotes) {
                        if (frequentlyUsedStickers.isNotEmpty()) {
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
                                            Image(
                                                painter = painterResource(id = history_icon),
                                                contentDescription = ""
                                            )
                                            Spacer(modifier = Modifier.width(10.dp))
                                            Text(
                                                "Frequently Used",
                                                fontSize = 20.sp,
                                                fontWeight = FontWeight(700)
                                            )
                                        }
                                    }
                                }
                            }
                            items(frequentlyUsedStickers) { sticker ->
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
                                                    context.setInputView(ComposeKeyboardStickersView(context))
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
                            item(span = { GridItemSpan(5) }) {
                                Spacer(modifier = Modifier.height(20.dp))
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
}

@Composable
fun BottomStickerBar(context: Context, repos: List<Repo>?, viewModel: RepoViewModel, bgSecondaryColor: Color, backspace_icon: Int, hideRepositories: Boolean) {
    val configuration = LocalConfiguration.current

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = {
            viewModel.deselectAllRepos()
            (context as IMEService).setInputView(ComposeKeyboardStickersView(context))
        }) {
            Image(
                painter = painterResource(id = R.drawable.app_icon),
                contentDescription = "",
                modifier = Modifier
                    .padding(10.dp)
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
        }
        if (hideRepositories) {
            Row(modifier = Modifier.width(configuration.screenWidthDp.dp - 110.dp)) {
                Text(text = "")
            }
        } else {
            LazyRow(modifier = Modifier.width(configuration.screenWidthDp.dp - 110.dp)) {
                if (repos != null) {
                    items(repos) { repo ->
                        if (repo.stickers != null) {
                            val repoIcon = repo.url + repo.icon
                            IconButton(onClick = {
                                viewModel.selectRepo(repo)
                                (context as IMEService).setInputView(ComposeKeyboardStickersRepoView(context))
                            }) {
                                NetworkKeyboardImage(
                                    imageURL = repoIcon,
                                    imageDescription = "",
                                    size = 40.dp,
                                    shape = CircleShape,
                                    bgSecondaryColor = bgSecondaryColor
                                )
                            }
                        }
                    }
                }
            }
        }
        IconButton(onClick = {
            val con = (context as IMEService)
            con.currentInputConnection.sendKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL))
            con.currentInputConnection.sendKeyEvent(KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DEL))
        }) {
            Image(
                painter = painterResource(id = backspace_icon),
                contentDescription = "",
                modifier = Modifier
                    .padding(10.dp)
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
        }
    }
}
