package com.paraskcd.nitroless

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.*
import androidx.navigation.compose.*
import com.paraskcd.nitroless.components.Drawer
import com.paraskcd.nitroless.ui.theme.*
import com.paraskcd.nitroless.utils.RememberMutableStateListOf
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var frequentlyUsedEmotes = RememberMutableStateListOf<String>()
            var isHomeActive by remember { mutableStateOf(false) }
            var isDrawerActive by remember { mutableStateOf(false) }
//            var isCommunityReposActive by remember { mutableStateOf(false) }
            val animateDrawer: Dp by animateDpAsState(
                if (isDrawerActive) 72.dp else 0.dp
            )
            NitrolessTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Navigation(
                            openDrawer = { isDrawerActive = it },
                            frequentlyUsedEmotes = frequentlyUsedEmotes,
                            animateDrawer = animateDrawer,
                            isDrawerActive = isDrawerActive
                        )
                        Drawer(
                            isHomeActive = isHomeActive,
                            isDrawerActive = isDrawerActive,
                            openDrawer = { isDrawerActive = it }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Navigation(openDrawer: (Boolean) -> Unit, frequentlyUsedEmotes: SnapshotStateList<String>, animateDrawer: Dp, isDrawerActive: Boolean) {
    val navController = rememberNavController()

    NavHost( navController = navController, startDestination = "home" ) {
        composable("home")
            {
                Home(
                    openDrawer = { openDrawer(true) },
                    closeDrawer = { openDrawer(false) },
                    navController = navController,
                    frequentlyUsedEmotes = frequentlyUsedEmotes,
                    animateDrawer = animateDrawer,
                    isDrawerActive = isDrawerActive
                )
            }
        composable("about") {
            About(
                navController = navController
            )
        }
    }
}