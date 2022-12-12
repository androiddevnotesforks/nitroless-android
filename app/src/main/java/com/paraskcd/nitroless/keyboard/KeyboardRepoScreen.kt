package com.paraskcd.nitroless.keyboard

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.paraskcd.nitroless.R
import com.paraskcd.nitroless.model.FrequentlyUsedEmotesTable
import com.paraskcd.nitroless.model.Repo
import com.paraskcd.nitroless.ui.theme.BGPrimaryDarkColor
import com.paraskcd.nitroless.ui.theme.BGSecondaryDarkColor
import com.paraskcd.nitroless.ui.theme.BGTertiaryDarkColor
import com.paraskcd.nitroless.ui.theme.TextDarkColor
import com.paraskcd.nitroless.viewmodel.RepoViewModel

@Composable
fun KeyboardRepoScreen(context: Context, selectedRepo: Repo?, viewModel: RepoViewModel, repos: List<Repo>?) {
    Column(
        modifier = Modifier
            .background(BGPrimaryDarkColor)
            .fillMaxWidth()
            .height(420.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        if (repos != null && selectedRepo != null) {
            Column(modifier = Modifier.height(360.dp)) {
                if (selectedRepo.favouriteEmotes != null && selectedRepo.favouriteEmotes!!.isNotEmpty()) {
                    FavouriteEmotes(context = context, viewModel = viewModel, favouriteEmotes = selectedRepo.favouriteEmotes!!)
                }
                SelectedRepo(context = context, selectedRepo = selectedRepo, viewModel = viewModel)
            }
            BottomBar(context = context, repos = repos, viewModel = viewModel)
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
fun SelectedRepo(context: Context, selectedRepo: Repo, viewModel: RepoViewModel) {
    val clipboardManager = LocalClipboardManager.current

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
                NetworkKeyboardImageWOPadding(
                    imageURL = selectedRepo.url + selectedRepo.icon,
                    imageDescription = "",
                    size = 25.dp,
                    shape = CircleShape
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    selectedRepo.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight(700)
                )
            }
            Spacer(modifier = Modifier.height(5.dp))
            LazyVerticalGrid(columns = GridCells.Adaptive(minSize = 48.dp), userScrollEnabled = true) {
                items(selectedRepo.emotes) { emote ->
                    val emoteURL = selectedRepo.url + selectedRepo.path + "/" + emote.name + "." + emote.type
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
                            size = 32.dp,
                            shape = RoundedCornerShape(10.dp)
                        )
                    }
                }
            }
        }
    }
}