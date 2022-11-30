package com.paraskcd.nitroless

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.*
import androidx.compose.ui.res.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.*
import androidx.navigation.*
import com.paraskcd.nitroless.elements.*

@Composable
fun Home( openDrawer: () -> Unit, closeDrawer: () -> Unit, navController: NavHostController, frequentlyUsedEmotes: SnapshotStateList<String>, animateDrawer: Dp, isDrawerActive: Boolean ) {

    Column(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colors.primary)
    ) {
        TopBar(
            titleName = null,
            buttonIcon = Icons.Filled.Menu,
            onNavButtonClicked = { openDrawer() },
            onButtonClicked = {
                if (isDrawerActive) {
                    closeDrawer()
                }
                navController.navigate("About") {
                    popUpTo("home")
                }
            }
        )
        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.background)
            .offset(
                x = animateDrawer
            )
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
        ) {
            item {
                Container {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(painter = painterResource(id = R.drawable.history_icon), contentDescription = "")
                            Spacer(modifier = Modifier.width(10.dp))
                            Text("Frequently Used Emotes", fontSize = 20.sp, fontWeight = FontWeight(700))
                        }
                        Spacer(modifier = Modifier.height(5.dp))
                        if (frequentlyUsedEmotes.isEmpty()) {
                            Text(text = "Start using Nitroless to show your frequently used emotes here.")
                        }
                    }
                }
            }
        }
    }
}
