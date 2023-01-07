package com.paraskcd.nitroless.keyboard

import android.content.*
import android.os.Build
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
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import com.paraskcd.nitroless.R
import com.paraskcd.nitroless.model.*
import com.paraskcd.nitroless.ui.theme.AccentColor
import com.paraskcd.nitroless.viewmodel.RepoViewModel

@Composable
fun KeyboardScreen(context: Context, repos: List<Repo>?, frequentlyUsedEmotes: List<FrequentlyUsedEmotesTable>, viewModel: RepoViewModel, favouriteEmotes: List<FavouriteEmotesTable>, textColor: Color, bgPrimaryColor: Color, bgSecondaryColor: Color, bgTertiaryColor: Color, history_icon: Int, backspace_icon: Int, hideRepositories: Boolean, hideFrequentlyUsedEmotes: Boolean, hideFavouriteEmotes: Boolean, frequentlyUsedStickers: List<FrequentlyUsedStickersTable>, favouriteStickers: List<FavouriteStickersTable>) {
    val clipboardManager = LocalClipboardManager.current

    Column(
        modifier = Modifier
            .background(bgPrimaryColor)
            .fillMaxWidth()
            .height(420.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        if (repos != null) {
            Column(modifier = Modifier.height(360.dp)) {
                LazyVerticalGrid(modifier = Modifier.fillMaxHeight(), columns = GridCells.Fixed(5)) {
                    if (favouriteStickers.isNotEmpty() || frequentlyUsedStickers.isNotEmpty()) {
                        item(span = { GridItemSpan(7) }) {
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
                                            if (index == 0) {
                                                (context as IMEService).setInputView(ComposeKeyboardView(context))
                                            } else {
                                                (context as IMEService).setInputView(ComposeKeyboardStickersView(context))
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
                    }
                    if (!hideFavouriteEmotes && favouriteEmotes.isNotEmpty()) {
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
                        items(favouriteEmotes) { emote ->
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
                    if (repos.isEmpty()) {
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
                                    Text("Welcome to Nitroless", fontSize = 18.sp, fontWeight = FontWeight(700))
                                    Spacer(modifier = Modifier.height(5.dp))
                                    Text(text = "Start using Nitroless by adding Your Repositories to the app. You'll need to go to the Nitroless App, tap on the Hamburger menu above to open the Sidebar Drawer and click either on the Globe Button or the Add Button.")
                                }
                            }
                        }
                        item(span = { GridItemSpan(5) }) {
                            Spacer(modifier = Modifier.height(20.dp))
                        }
                    } else {
                        if (!hideFrequentlyUsedEmotes) {
                            if (frequentlyUsedEmotes.isEmpty()) {
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
                                                    fontSize = 18.sp,
                                                    fontWeight = FontWeight(700)
                                                )
                                            }
                                            Spacer(modifier = Modifier.height(5.dp))
                                            Text(text = "Start using Nitroless to show your frequently used emotes here.")
                                        }
                                    }
                                }
                                item(span = { GridItemSpan(5) }) {
                                    Spacer(modifier = Modifier.height(20.dp))
                                }
                            } else {
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
                                items(frequentlyUsedEmotes) { emote ->
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
                        }
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

@Composable
fun BottomBar(context: Context, repos: List<Repo>?, viewModel: RepoViewModel, bgSecondaryColor: Color, backspace_icon: Int, hideRepositories: Boolean) {
    val configuration = LocalConfiguration.current

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = {
            viewModel.deselectAllRepos()
            (context as IMEService).setInputView(ComposeKeyboardView(context))
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
                        val repoIcon = repo.url + repo.icon
                        IconButton(onClick = {
                            viewModel.selectRepo(repo)
                            (context as IMEService).setInputView(ComposeKeyboardRepoView(context))
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

@Composable
fun NetworkKeyboardImage(imageURL: String, imageDescription: String, size: Dp, shape: Shape, bgSecondaryColor: Color) {
    val context = LocalContext.current

    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(context = context).data(imageURL).diskCacheKey(imageURL)
            .memoryCacheKey(imageURL).build(),
        imageLoader = ImageLoader.Builder(context = context).components {
            if (Build.VERSION.SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }.build()
    )

    Image(
        painter = painter,
        contentDescription = imageDescription,
        modifier = Modifier
            .padding(10.dp)
            .clip(shape)
            .size(size)
            .background(bgSecondaryColor)
    )
}

@Composable
fun NetworkKeyboardImageWOPadding(imageURL: String, imageDescription: String, size: Dp, shape: Shape, bgSecondaryColor: Color) {
    val context = LocalContext.current

    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(context = context).data(imageURL).diskCacheKey(imageURL)
            .memoryCacheKey(imageURL).build(),
        imageLoader = ImageLoader.Builder(context = context).components {
            if (Build.VERSION.SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }.build()
    )

    Image(
        painter = painter,
        contentDescription = imageDescription,
        modifier = Modifier
            .clip(shape)
            .size(size)
            .background(bgSecondaryColor)
    )
}