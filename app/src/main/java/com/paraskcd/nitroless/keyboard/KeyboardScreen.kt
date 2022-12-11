package com.paraskcd.nitroless.keyboard

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.text.TextUtils
import android.view.KeyEvent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
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
import androidx.core.content.ContextCompat.startActivity
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import com.paraskcd.nitroless.R
import com.paraskcd.nitroless.model.Emote
import com.paraskcd.nitroless.model.FrequentlyUsedEmotesTable
import com.paraskcd.nitroless.model.Repo
import com.paraskcd.nitroless.ui.theme.*
import com.paraskcd.nitroless.viewmodel.RepoViewModel

@Composable
fun KeyboardScreen(context: Context, repos: List<Repo>?, frequentlyUsedEmotes: List<FrequentlyUsedEmotesTable>, selectedRepo: Repo, viewModel: RepoViewModel) {
    Column(
        modifier = Modifier
            .background(BGPrimaryDarkColor)
            .fillMaxWidth()
            .height(360.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        if (repos != null) {
            LazyColumn(
                modifier = Modifier.height(300.dp)
            ) {
                item {
                    Home(context = context, viewModel = viewModel, frequentlyUsedEmotes = frequentlyUsedEmotes, repoEmptyFlag = repos.isEmpty())
                }
                if (repos.isNotEmpty()) {
                    items(repos) {
                            repo ->
                        RepoView(context = context, viewModel = viewModel, emotes = repo.emotes, name = repo.name, path = repo.path, url = repo.url!!, icon = repo.icon)
                    }
                }
            }
            
            BottomBar(context = context, repos = repos)
        } else {
            Card(
                backgroundColor = BGSecondaryDarkColor,
                contentColor = TextDarkColor,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
                    .padding(bottom = 5.dp)
                    .padding(horizontal = 10.dp),
                border = BorderStroke(
                    width = 1.dp,
                    color = BGTertiaryDarkColor.copy(alpha = 0.1F)
                ),
                shape = RoundedCornerShape(20.dp)
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
fun RepoView(context: Context, viewModel: RepoViewModel, emotes: List<Emote>, name: String, path: String, url: String, icon: String) {
    val clipboardManager = LocalClipboardManager.current

    Card(
        backgroundColor = BGSecondaryDarkColor,
        contentColor = TextDarkColor,
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .padding(top = 10.dp)
            .padding(bottom = 5.dp)
            .padding(horizontal = 10.dp),
        border = BorderStroke(
            width = 1.dp,
            color = BGTertiaryDarkColor.copy(alpha = 0.1F)
        ),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                NetworkKeyboardImage(imageURL = url + icon, imageDescription = "", size = 35.dp, shape = RoundedCornerShape(8.dp))
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight(700)
                )
            }
            Spacer(modifier = Modifier.height(5.dp))
            LazyVerticalGrid(columns = GridCells.Adaptive(minSize = 56.dp), userScrollEnabled = true) {
                items(emotes) { emote ->
                    val emoteURL = url + path + "/" + emote.name + "." + emote.type
                    IconButton(
                        onClick = {
                            viewModel.addFrequentlyUsedEmote(
                                emote = FrequentlyUsedEmotesTable(emoteURL = emoteURL)
                            )
                            clipboardManager.setText(AnnotatedString(emoteURL))
                            (context as IMEService).currentInputConnection.commitText(emoteURL, emoteURL.length)
                        }
                    ) {
                        NetworkKeyboardImage(
                            imageURL = emoteURL,
                            imageDescription = emoteURL,
                            size = 40.dp,
                            shape = RoundedCornerShape(10.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BottomBar(context: Context, repos: List<Repo>?) {
    val configuration = LocalConfiguration.current
    val ctx = LocalContext.current

    val intent = ctx.getPackageManager().getLaunchIntentForPackage("com.paraskcd.nitroless")

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = {
            ctx.startActivity(intent)
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
        Row(modifier = Modifier.width(configuration.screenWidthDp.dp - 110.dp)){}
        IconButton(onClick = {
            val con = (context as IMEService)
            con.currentInputConnection.sendKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL))
            con.currentInputConnection.sendKeyEvent(KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DEL))
        }) {
            Image(
                painter = painterResource(id = R.drawable.backspace),
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
fun Home(context: Context, viewModel: RepoViewModel, frequentlyUsedEmotes: List<FrequentlyUsedEmotesTable>, repoEmptyFlag: Boolean) {
    val clipboardManager = LocalClipboardManager.current
    if (repoEmptyFlag) {
        Card(
            backgroundColor = BGSecondaryDarkColor,
            contentColor = TextDarkColor,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
                .padding(bottom = 5.dp)
                .padding(horizontal = 10.dp),
            border = BorderStroke(
                width = 1.dp,
                color = BGTertiaryDarkColor.copy(alpha = 0.1F)
            ),
            shape = RoundedCornerShape(20.dp)
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
    } else {
        if (frequentlyUsedEmotes.isEmpty()) {
            Card(
                backgroundColor = BGSecondaryDarkColor,
                contentColor = TextDarkColor,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
                    .padding(bottom = 5.dp)
                    .padding(horizontal = 10.dp),
                border = BorderStroke(
                    width = 1.dp,
                    color = BGTertiaryDarkColor.copy(alpha = 0.1F)
                ),
                shape = RoundedCornerShape(20.dp)
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
                            painter = painterResource(id = R.drawable.history_icon_keyboard),
                            contentDescription = ""
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            "Frequently Used Emotes",
                            fontSize = 18.sp,
                            fontWeight = FontWeight(700)
                        )
                    }
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(text = "Start using Nitroless to show your frequently used emotes here.")
                }
            }
        } else {
            Card(
                backgroundColor = BGSecondaryDarkColor,
                contentColor = TextDarkColor,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .padding(top = 10.dp)
                    .padding(bottom = 5.dp)
                    .padding(horizontal = 10.dp),
                border = BorderStroke(
                    width = 1.dp,
                    color = BGTertiaryDarkColor.copy(alpha = 0.1F)
                ),
                shape = RoundedCornerShape(20.dp)
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
                            painter = painterResource(id = R.drawable.history_icon_keyboard),
                            contentDescription = ""
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            "Frequently Used Emotes",
                            fontSize = 18.sp,
                            fontWeight = FontWeight(700)
                        )
                    }
                    Spacer(modifier = Modifier.height(5.dp))
                    LazyVerticalGrid(columns = GridCells.Adaptive(minSize = 56.dp), userScrollEnabled = true) {
                        items(frequentlyUsedEmotes.reversed()) { emote ->
                            IconButton(
                                onClick = {
                                    val emoteURL = emote.emoteURL
                                    viewModel.addFrequentlyUsedEmote(
                                        emote = FrequentlyUsedEmotesTable(emoteURL = emoteURL)
                                    )
                                    clipboardManager.setText(AnnotatedString(emoteURL))
                                    (context as IMEService).currentInputConnection.commitText(emoteURL, emoteURL.length)
                                }
                            ) {
                                NetworkKeyboardImage(
                                    imageURL = emote.emoteURL,
                                    imageDescription = emote.emoteURL,
                                    size = 40.dp,
                                    shape = RoundedCornerShape(10.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NetworkKeyboardImage(imageURL: String, imageDescription: String, size: Dp, shape: Shape) {
    val context = LocalContext.current

    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(context = context).data(imageURL).build(),
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
            .background(BGSecondaryDarkColor)
    )
}