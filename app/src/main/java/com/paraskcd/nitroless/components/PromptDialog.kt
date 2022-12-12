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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.paraskcd.nitroless.model.Emote
import com.paraskcd.nitroless.model.FavouriteEmotesTable
import com.paraskcd.nitroless.model.FrequentlyUsedEmotesTable
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
                            "Remove Repository - ${repoName}",
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
    selectedEmote: FavouriteEmotesTable,
    viewModel: RepoViewModel,
    refresh: () -> Unit
) {
    var exists by remember {
        mutableStateOf(false)
    }

    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current
    val favouriteEmotes = viewModel.favouriteEmotes.collectAsState().value

    favouriteEmotes.forEach {
        exists = it == selectedEmote
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
                                Text(text = "Copy")
                            }
                            if (exists) {
                                Button(
                                    onClick = {
                                        viewModel.deleteFavouriteEmote(selectedEmote)
                                        refresh()
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
                                    Text(text = "Remove from Favourites")
                                }
                            } else {
                                Button(
                                    onClick = {
                                        viewModel.addFavouriteEmote(selectedEmote)
                                        refresh()
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
                                    Text(text = "Add to Favourites")
                                }
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
                                    .width(250.dp)
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