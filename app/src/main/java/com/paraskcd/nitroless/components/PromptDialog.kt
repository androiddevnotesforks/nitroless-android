package com.paraskcd.nitroless.components

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.paraskcd.nitroless.model.*
import com.paraskcd.nitroless.ui.theme.AccentColor
import com.paraskcd.nitroless.ui.theme.Danger
import com.paraskcd.nitroless.utils.NetworkImage
import com.paraskcd.nitroless.viewmodel.RepoViewModel

@Composable
fun DeleteRepoPromptDialog(
    show: Boolean,
    deleteRepoOnClick: () -> Unit,
    cancelButtonOnClick: () -> Unit,
    repoName: String
) {
    BackHandler(enabled = show) {
        cancelButtonOnClick()
    }
    AnimatedVisibility(
        visible = show,
        enter =  fadeIn(),
        exit = fadeOut()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xA5000000)),
            contentAlignment = Alignment.Center
        ) {
            AnimatedVisibility(
                visible = show,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                DarkContainer {
                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)) {
                        Text(
                            "Remove Repository - $repoName",
                            fontSize = 20.sp,
                            fontWeight = FontWeight(700)
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(
                                onClick = {
                                    deleteRepoOnClick()
                                },
                                shape = CircleShape,
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = AccentColor,
                                    contentColor = Color.White
                                ),
                                modifier = Modifier
                                    .width(120.dp)
                                    .padding(horizontal = 10.dp)
                            ) {
                                Text(text = "Remove")
                            }
                            Button(
                                onClick = {
                                    cancelButtonOnClick()
                                },
                                shape = CircleShape,
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = Danger,
                                    contentColor = Color.White
                                ),
                                modifier = Modifier
                                    .width(120.dp)
                                    .padding(horizontal = 10.dp)
                            ) {
                                Text(text = "Cancel")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AddRepoPromptDialog(
    show: Boolean,
    addRepo: (String) -> Unit,
    repoToAdd: String,
    addRepoButtonOnClick: () -> Unit,
    cancelButtonOnClick: () -> Unit
) {
    BackHandler(enabled = show) {
        cancelButtonOnClick()
    }
    AnimatedVisibility(
        visible = show,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xA5000000)),
            contentAlignment = Alignment.Center
        ) {
            AnimatedVisibility(
                visible = show,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                DarkContainer {
                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)) {
                        Text(
                            "Add Repository",
                            fontSize = 20.sp,
                            fontWeight = FontWeight(700)
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        TextField(
                            value = repoToAdd,
                            onValueChange = { value ->
                                addRepo(value)
                            },
                            shape = CircleShape,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            colors = TextFieldDefaults.textFieldColors(
                                textColor = MaterialTheme.colors.onPrimary,
                                disabledTextColor = Color.Transparent,
                                backgroundColor = MaterialTheme.colors.secondary,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent
                            ),
                            placeholder = {
                                Text(text = "Repository URL")
                            }
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(
                                onClick = {
                                    addRepoButtonOnClick()
                                },
                                shape = CircleShape,
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = AccentColor,
                                    contentColor = Color.White
                                ),
                                modifier = Modifier
                                    .width(120.dp)
                                    .padding(horizontal = 10.dp)
                            ) {
                                Text(text = "Add")
                            }
                            Button(
                                onClick = {
                                    cancelButtonOnClick()
                                },
                                shape = CircleShape,
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = Danger,
                                    contentColor = Color.White
                                ),
                                modifier = Modifier
                                    .width(120.dp)
                                    .padding(horizontal = 10.dp)
                            ) {
                                Text(text = "Cancel")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ContextMenuPrompt(
    show: Boolean,
    cancelButtonOnClick: () -> Unit,
    selectedEmote: FavouriteEmotesTable?,
    selectedSticker: FavouriteStickersTable?,
    viewModel: RepoViewModel,
    refresh: () -> Unit
) {
    var exists by remember {
        mutableStateOf(false)
    }

    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current
    val favouriteEmotes = viewModel.favouriteEmotes.collectAsState().value
    val favouriteStickers = viewModel.favouriteStickers.collectAsState().value

    if (selectedEmote != null) {
        favouriteEmotes.forEach {
            exists = it == selectedEmote
        }
    }

    if (selectedSticker != null) {
        favouriteStickers.forEach {
            exists = it == selectedSticker
        }
    }

    BackHandler(enabled = show) {
        cancelButtonOnClick()
    }

    AnimatedVisibility(
        visible = show,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xA5000000)),
            contentAlignment = Alignment.Center
        ) {
            AnimatedVisibility(
                visible = show,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                if (selectedEmote != null) {
                    Column(
                        modifier = Modifier
                            .width(400.dp)
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        NetworkImage(imageURL = selectedEmote.emoteURL, imageDescription = "", size = 50.dp, shape = RoundedCornerShape(10.dp))
                        DarkContainer {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Button(
                                    onClick = {
                                        val emoteURL = selectedEmote.emoteURL
                                        viewModel.addFrequentlyUsedEmote(
                                            emote = FrequentlyUsedEmotesTable(emoteURL = emoteURL)
                                        )
                                        clipboardManager.setText(AnnotatedString(emoteURL))
                                        Toast.makeText(
                                            context,
                                            "Copied Emote",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        viewModel.deselectEmote()
                                        cancelButtonOnClick()
                                    },
                                    shape = CircleShape,
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = AccentColor,
                                        contentColor = Color.White
                                    ),
                                    modifier = Modifier
                                        .width(250.dp)
                                        .padding(horizontal = 10.dp)
                                ) {
                                    Text(text = "Copy", textAlign = TextAlign.Center)
                                }
                                if (exists) {
                                    Button(
                                        onClick = {
                                            viewModel.deleteFavouriteEmote(selectedEmote)
                                            refresh()
                                            viewModel.deselectEmote()
                                            cancelButtonOnClick()
                                        },
                                        shape = CircleShape,
                                        colors = ButtonDefaults.buttonColors(
                                            backgroundColor = AccentColor,
                                            contentColor = Color.White
                                        ),
                                        modifier = Modifier
                                            .width(250.dp)
                                            .padding(horizontal = 10.dp)
                                    ) {
                                        Text(text = "Remove from Favourite Emotes", textAlign = TextAlign.Center)
                                    }
                                } else {
                                    Button(
                                        onClick = {
                                            viewModel.addFavouriteEmote(selectedEmote)
                                            refresh()
                                            viewModel.deselectEmote()
                                            cancelButtonOnClick()
                                        },
                                        shape = CircleShape,
                                        colors = ButtonDefaults.buttonColors(
                                            backgroundColor = AccentColor,
                                            contentColor = Color.White
                                        ),
                                        modifier = Modifier
                                            .width(250.dp)
                                            .padding(horizontal = 10.dp)
                                    ) {
                                        Text(text = "Add to Favourite Emotes", textAlign = TextAlign.Center)
                                    }
                                }
                                Button(
                                    onClick = {
                                        viewModel.deselectEmote()
                                        cancelButtonOnClick()
                                    },
                                    shape = CircleShape,
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = Danger,
                                        contentColor = Color.White
                                    ),
                                    modifier = Modifier
                                        .width(250.dp)
                                        .padding(horizontal = 10.dp)
                                ) {
                                    Text(text = "Cancel", textAlign = TextAlign.Center)
                                }
                            }
                        }
                    }
                }
                if (selectedSticker != null) {
                    Column(
                        modifier = Modifier
                            .width(400.dp)
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        NetworkImage(imageURL = selectedSticker.stickerURL, imageDescription = "", size = 72.dp, shape = RoundedCornerShape(10.dp))
                        DarkContainer {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Button(
                                    onClick = {
                                        val stickerURL = selectedSticker.stickerURL
                                        viewModel.addFrequentlyUsedSticker(
                                            sticker = FrequentlyUsedStickersTable(stickerURL = stickerURL)
                                        )
                                        clipboardManager.setText(AnnotatedString(stickerURL))
                                        Toast.makeText(
                                            context,
                                            "Copied Sticker",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        viewModel.deselectSticker()
                                        cancelButtonOnClick()
                                    },
                                    shape = CircleShape,
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = AccentColor,
                                        contentColor = Color.White
                                    ),
                                    modifier = Modifier
                                        .width(290.dp)
                                        .padding(horizontal = 10.dp)
                                ) {
                                    Text(text = "Copy", textAlign = TextAlign.Center)
                                }
                                if (exists) {
                                    Button(
                                        onClick = {
                                            viewModel.deleteFavouriteSticker(selectedSticker)
                                            refresh()
                                            viewModel.deselectSticker()
                                            cancelButtonOnClick()
                                        },
                                        shape = CircleShape,
                                        colors = ButtonDefaults.buttonColors(
                                            backgroundColor = AccentColor,
                                            contentColor = Color.White
                                        ),
                                        modifier = Modifier
                                            .width(290.dp)
                                            .padding(horizontal = 10.dp)
                                    ) {
                                        Text(text = "Remove from Favourite Stickers", textAlign = TextAlign.Center)
                                    }
                                } else {
                                    Button(
                                        onClick = {
                                            viewModel.addFavouriteSticker(selectedSticker)
                                            refresh()
                                            viewModel.deselectSticker()
                                            cancelButtonOnClick()
                                        },
                                        shape = CircleShape,
                                        colors = ButtonDefaults.buttonColors(
                                            backgroundColor = AccentColor,
                                            contentColor = Color.White
                                        ),
                                        modifier = Modifier
                                            .width(290.dp)
                                            .padding(horizontal = 10.dp)
                                    ) {
                                        Text(text = "Add to Favourite Stickers", textAlign = TextAlign.Center)
                                    }
                                }
                                Button(
                                    onClick = {
                                        viewModel.deselectSticker()
                                        cancelButtonOnClick()
                                    },
                                    shape = CircleShape,
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = Danger,
                                        contentColor = Color.White
                                    ),
                                    modifier = Modifier
                                        .width(290.dp)
                                        .padding(horizontal = 10.dp)
                                ) {
                                    Text(text = "Cancel", textAlign = TextAlign.Center)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}