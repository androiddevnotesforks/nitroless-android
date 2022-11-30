package com.paraskcd.nitroless

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.paraskcd.nitroless.elements.TopBar
import com.paraskcd.nitroless.ui.theme.NitrolessTheme

@Composable
fun Home(openDrawer: () -> Unit, navController: NavHostController) {
    Column(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colors.primary)
    ) {
        TopBar(titleName = null, buttonIcon = Icons.Filled.Menu, onNavButtonClicked = { openDrawer() }, onButtonClicked = { navController.navigate("About") {
            popUpTo("home")
        } })
    }
}
