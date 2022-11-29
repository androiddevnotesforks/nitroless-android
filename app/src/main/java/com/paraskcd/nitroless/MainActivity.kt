package com.paraskcd.nitroless

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.paraskcd.nitroless.ui.theme.NitrolessTheme

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
            NitrolessTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .background(color = MaterialTheme.colors.primary)
                    ) {
                        TopBar(buttonIcon = Icons.Filled.Menu, onButtonClicked = {})
                        About()
                    }

                }
            }
        }
    }
}

@Composable
fun TopBar(buttonIcon: ImageVector, onButtonClicked: () -> Unit) {
    TopAppBar(
        elevation = 12.dp,
        title = {
            Row(modifier = Modifier
                .fillMaxWidth()
                .height(72.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                Image(
                    painter = painterResource(id = R.drawable.banner),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(248.dp)
                        .padding(horizontal = 10.dp),
                    contentScale = ContentScale.Fit,
                    alignment = Alignment.Center
                )
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(Icons.Filled.Info, contentDescription = "")
                }
            }
        },
        navigationIcon = {
            IconButton(onClick = { onButtonClicked() } ) {
                Icon(buttonIcon, contentDescription = "")
            }
        },
        backgroundColor = MaterialTheme.colors.primaryVariant
    )
}

@Composable
fun Drawer(modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .background(color = MaterialTheme.colors.primaryVariant)
            .width(72.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            Box(
                modifier = Modifier
                    .width(5.dp)
                    .height(32.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colors.onPrimary)
            )
            IconButton(onClick = { /*TODO*/ }) {
                Image(painter = painterResource(id = R.drawable.app_icon), contentDescription = "", modifier = Modifier
                    .padding(10.dp)
                    .clip(
                        RoundedCornerShape(8.dp)
                    ))
            }
        }
        Divider(modifier = Modifier.padding(horizontal = 10.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    NitrolessTheme {
        Drawer()
    }
}