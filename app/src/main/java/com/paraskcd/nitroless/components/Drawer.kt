package com.paraskcd.nitroless.components

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.paraskcd.nitroless.ui.theme.*

@Composable
fun Drawer(isHomeActive: Boolean, isDrawerActive: Boolean, openDrawer: (Boolean) -> Unit, openCommunityRepos: (Boolean) -> Unit) {
    val density = LocalDensity.current
    AnimatedVisibility(
        visible = isDrawerActive,
        enter =
        slideInHorizontally {
            with(density) { -40.dp.roundToPx() }
        } + expandHorizontally(expandFrom = Alignment.Start) + fadeIn(initialAlpha = 0.3f),
        exit = slideOutHorizontally() + shrinkHorizontally() + fadeOut()
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxHeight()
                .background(color = MaterialTheme.colors.primaryVariant)
                .width(72.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            item {
                IconButton(
                    onClick = { openDrawer(false) },
                    modifier = Modifier
                        .padding(bottom = 5.dp)
                        .size(50.dp)
                        .clip(CircleShape)
                        .offset(y = 5.dp)
                ) {
                    Icon(
                        Icons.Rounded.Close,
                        contentDescription = "Close Button"
                    )
                }
            }
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        modifier = Modifier
                            .width(5.dp)
                            .height(32.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(MaterialTheme.colors.onPrimary)
                    )
                    IconButton(onClick = { /*TODO*/ }) {
                        Image(
                            painter = painterResource(id = com.paraskcd.nitroless.R.drawable.app_icon),
                            contentDescription = "",
                            modifier = Modifier
                                .padding(10.dp)
                                .clip(
                                    RoundedCornerShape(8.dp)
                                )
                        )
                    }
                }
            }
            item {
                Divider(
                    modifier = Modifier
                        .padding(horizontal = 15.dp)
                        .offset(x = 2.dp)
                )
            }
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        modifier = Modifier
                            .width(5.dp)
                            .background(Color.Transparent)
                    )
                    IconButton(
                        onClick = {
                            openDrawer(false)
                            openCommunityRepos(true)
                        },
                        modifier = Modifier
                            .padding(vertical = 10.dp)
                            .clip(CircleShape)
                            .background(AccentColor)
                            .size(50.dp)
                    ) {
                        Image(
                            painter = painterResource(id = com.paraskcd.nitroless.R.drawable.globe2),
                            contentDescription = "",
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }
            }
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        modifier = Modifier
                            .width(5.dp)
                            .background(Color.Transparent)
                    )
                    IconButton(
                        onClick = { /*TODO*/ },
                        modifier = Modifier
                            .padding(bottom = 10.dp)
                            .clip(CircleShape)
                            .background(
                                Success
                            )
                            .size(50.dp)
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
}