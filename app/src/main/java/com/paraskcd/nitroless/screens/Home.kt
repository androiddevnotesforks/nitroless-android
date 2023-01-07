package com.paraskcd.nitroless

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
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
import androidx.compose.runtime.remember
import androidx.compose.ui.*
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.*
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.*
import androidx.navigation.*
import coil.annotation.ExperimentalCoilApi
import coil.imageLoader
import coil.memory.MemoryCache
import com.paraskcd.nitroless.components.*
import com.paraskcd.nitroless.enums.RepoPage
import com.paraskcd.nitroless.model.FavouriteEmotesTable
import com.paraskcd.nitroless.model.FavouriteStickersTable
import com.paraskcd.nitroless.model.FrequentlyUsedEmotesTable
import com.paraskcd.nitroless.model.FrequentlyUsedStickersTable
import com.paraskcd.nitroless.utils.NetworkImage
import com.paraskcd.nitroless.viewmodel.RepoViewModel
import java.io.IOException
import java.util.*
import kotlin.concurrent.schedule
import kotlin.concurrent.timerTask

@OptIn(ExperimentalCoilApi::class)
@Composable
fun Home( openDrawer: () -> Unit, closeDrawer: () -> Unit, navController: NavHostController, isDrawerActive: Boolean, viewModel: RepoViewModel, frequentlyUsedEmotes: List<FrequentlyUsedEmotesTable>, repoEmptyFlag: Boolean, favouriteEmotes: List<FavouriteEmotesTable>, frequentlyUsedStickers: List<FrequentlyUsedStickersTable>, favouriteStickers: List<FavouriteStickersTable>, repoMenu: Int, onClickRepoMenu: (Int) -> Unit, isCommunityReposActive: Boolean ) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val activity = (LocalContext.current as? Activity)
    val imageLoader = context.imageLoader
    val interactionSource = remember { MutableInteractionSource() }

    BackHandler(enabled = !isCommunityReposActive) {
        if (repoMenu == RepoPage.STICKERS.value) {
            onClickRepoMenu(2)
            Timer().schedule(500) {
                onClickRepoMenu(RepoPage.EMOTES.value)
            }
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
            .clickable(
                onClick = {
                    if (isDrawerActive) {
                        closeDrawer()
                    }
                },
                interactionSource = interactionSource,
                indication = null
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
                    Picker(
                        repoMenu = repoMenu,
                        onClickRepoMenu = {
                                value ->
                            onClickRepoMenu(2)
                            Timer().schedule(500) {
                                onClickRepoMenu(value)
                            }
                            if (value == RepoPage.STICKERS.value) {
                                for (emote in favouriteEmotes) {
                                    val url = emote.emoteURL
                                    imageLoader.diskCache?.remove(url)
                                    imageLoader.memoryCache?.remove(MemoryCache.Key(url))
                                }
                                for (emote in frequentlyUsedEmotes) {
                                    val url = emote.emoteURL
                                    imageLoader.diskCache?.remove(url)
                                    imageLoader.memoryCache?.remove(MemoryCache.Key(url))
                                }
                            }
                            if (value == RepoPage.EMOTES.value) {
                                for (sticker in favouriteStickers) {
                                    val url = sticker.stickerURL
                                    imageLoader.diskCache?.remove(url)
                                    imageLoader.memoryCache?.remove(MemoryCache.Key(url))
                                }
                                for (sticker in frequentlyUsedStickers) {
                                    val url = sticker.stickerURL
                                    imageLoader.diskCache?.remove(url)
                                    imageLoader.memoryCache?.remove(MemoryCache.Key(url))
                                }
                            }
                        }
                    )
                }
            }
            if (repoMenu == 2) {
                item(span = { GridItemSpan(5) }) {
                    Box(modifier = Modifier.width(30.dp).height(30.dp).padding(50.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = MaterialTheme.colors.onPrimary)
                    }
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
                                    context.imageLoader.diskCache?.get(stickerURL)?.use { snapshot ->
                                        val imageFile = snapshot.data.toFile()
                                        val mClipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                        val values = ContentValues().apply {
                                            put(MediaStore.MediaColumns.DATA, imageFile.absolutePath)
                                            put(MediaStore.MediaColumns.DISPLAY_NAME, "NitrolessEmote")
                                            put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
                                            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DCIM)
                                        }
                                        val resolver = context.contentResolver
                                        val imageURI = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                                        val clipData = ClipData.newUri(resolver, "Nitroless Emote", imageURI)
                                        resolver.openOutputStream(imageURI!!)?.use {
                                            if (!BitmapFactory.decodeFile(imageFile.path).compress(
                                                    Bitmap.CompressFormat.PNG, 100, it))
                                                throw IOException("Failed to save bitmap.")
                                        } ?: throw IOException("Failed to open output stream.")
                                        mClipboard.setPrimaryClip(clipData)
                                    }
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
                                    context.imageLoader.diskCache?.get(stickerURL)?.use { snapshot ->
                                        val imageFile = snapshot.data.toFile()
                                        val mClipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                        val values = ContentValues().apply {
                                            put(MediaStore.MediaColumns.DATA, imageFile.absolutePath)
                                            put(MediaStore.MediaColumns.DISPLAY_NAME, "NitrolessEmote")
                                            put(MediaStore.MediaColumns.MIME_TYPE, "image/webp")
                                            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DCIM)
                                        }
                                        val resolver = context.contentResolver
                                        val imageURI = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                                        val clipData = ClipData.newUri(resolver, "Nitroless Emote", imageURI)
                                        resolver.openOutputStream(imageURI!!)?.use {
                                            if (!BitmapFactory.decodeFile(imageFile.path).compress(
                                                    Bitmap.CompressFormat.WEBP, 100, it))
                                                throw IOException("Failed to save bitmap.")
                                        } ?: throw IOException("Failed to open output stream.")
                                        mClipboard.setPrimaryClip(clipData)
                                    }
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
