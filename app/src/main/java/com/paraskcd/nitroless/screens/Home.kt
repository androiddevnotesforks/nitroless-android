package com.paraskcd.nitroless

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.paraskcd.nitroless.elements.Container
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
        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.background)
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
                    }
                }
            }
        }
    }
}
