package com.paraskcd.nitroless

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.paraskcd.nitroless.elements.TopBar
import com.paraskcd.nitroless.ui.theme.AccentColor
import com.paraskcd.nitroless.ui.theme.NitrolessTheme
import com.paraskcd.nitroless.ui.theme.Success

sealed class HomeScreens(val title: String) {
    object Home: HomeScreens("Home")
    object About: HomeScreens("About")
}

sealed class DrawerScreens(val title: String) {
    object Home: HomeScreens("Home")
    object Repo: DrawerScreens("Repo")
}

private val screens = listOf(
    DrawerScreens.Home,
    DrawerScreens.Repo
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var isHomeActive by remember { mutableStateOf(false) }
            var isDrawerActive by remember { mutableStateOf(false) }

            NitrolessTheme {

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Navigation(openDrawer = { isDrawerActive = it })
                        Drawer(isHomeActive = isHomeActive, isDrawerActive = isDrawerActive, openDrawer = { isDrawerActive = it })
                    }
                }
            }
        }
    }
}

@Composable
fun Navigation(openDrawer: (Boolean) -> Unit) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home" ) {
        composable("home") { Home(openDrawer = { openDrawer(true) }, navController = navController) }
        composable("about") { About(navController = navController) }
    }
}

@Composable
fun Drawer(isHomeActive: Boolean, isDrawerActive: Boolean, openDrawer: (Boolean) -> Unit) {
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
                            painter = painterResource(id = R.drawable.app_icon),
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
                        onClick = { /*TODO*/ },
                        modifier = Modifier
                            .padding(vertical = 10.dp)
                            .clip(CircleShape)
                            .background(AccentColor)
                            .size(50.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.globe),
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