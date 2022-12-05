package com.paraskcd.nitroless.components

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.paraskcd.nitroless.R
import com.paraskcd.nitroless.model.Repo
import com.paraskcd.nitroless.model.RepoTable
import com.paraskcd.nitroless.ui.theme.AccentColor
import com.paraskcd.nitroless.utils.NetworkImage
import com.paraskcd.nitroless.viewmodel.RepoViewModel

@Composable
fun CommunityReposUI(isCommunityReposActive: Boolean, viewModel: RepoViewModel, openCommunityRepos: (Boolean) -> Unit) {
    val density = LocalDensity.current
    val communityRepos = viewModel.communityRepos.observeAsState()
    val loadingCommunityRepos = viewModel.loadingCommunityRepos.observeAsState()

    AnimatedVisibility(
        visible = isCommunityReposActive,
        enter = slideInVertically {
            with(density) { -40.dp.roundToPx() }
        } + expandVertically(expandFrom = Alignment.Top) + fadeIn(initialAlpha = 0.3f),
        exit = slideOutVertically() + shrinkVertically() + fadeOut()
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.primaryVariant)
                .padding(25.dp)
                .clip(RoundedCornerShape(20.dp)),
            horizontalAlignment = Alignment.Start
        ) {
            item {
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            openCommunityRepos(false)
                        },
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                    ) {
                        Icon(
                            Icons.Rounded.Close,
                            contentDescription = "Close Button"
                        )
                    }
                    Row(modifier = Modifier.padding(horizontal = 5.dp), verticalAlignment = Alignment.CenterVertically) {
                        Image(painter = painterResource(id = R.drawable.globe), contentDescription = "Community Repos", contentScale = ContentScale.Fit, modifier = Modifier
                            .size(30.dp)
                            .padding(end = 10.dp))
                        Text(text = "Community Repos", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
            if (loadingCommunityRepos.value == true) {
                item {
                    Column(modifier = Modifier.fillMaxWidth().padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator()
                    }
                }
            } else {
                if (communityRepos.value != null) {
                    items(communityRepos.value!!) {
                            communityRepo ->
                        CommunityRepoRow(communityRepo = communityRepo, onAddRepo = {}, onRemoveRepo = {}, modifier = Modifier.padding(10.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun CommunityRepoRow(modifier: Modifier = Modifier, communityRepo: Repo, onAddRepo: (RepoTable) -> Unit, onRemoveRepo: (RepoTable) -> Unit) {
    val context = LocalContext.current
    ContainerPill {
        Box(modifier = modifier) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    NetworkImage(imageURL = communityRepo.url + communityRepo.icon, imageDescription = communityRepo.name, size = 50.dp, shape = CircleShape,)
                    Column(modifier = Modifier.padding(horizontal = 10.dp)) {
                        Text(text = communityRepo.name, fontWeight = FontWeight.Black, fontSize = 18.sp)
                        if (communityRepo.author != null) {
                            Text(text = "By ${communityRepo.author}", fontWeight = FontWeight.Light, fontSize = 12.sp)
                        }
                        Text(text = "${communityRepo.emotes.size} Emotes", fontWeight = FontWeight.Light, fontSize = 12.sp)
                    }
                }
                IconButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .padding(10.dp)
                        .clip(CircleShape)
                        .background(
                            AccentColor
                        )
                        .size(32.dp)
                ) {
                    Icon(
                        Icons.Rounded.AddCircle,
                        contentDescription = "",
                        modifier = Modifier.padding(5.dp),
                        tint = Color.White
                    )
                }
            }
        }
    }
}