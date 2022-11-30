package com.paraskcd.nitroless.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun TopBar(titleName: String?, buttonIcon: ImageVector, onNavButtonClicked: () -> Unit, onButtonClicked: () -> Unit) {
    TopAppBar(
        elevation = 12.dp,
        title = {
            if(titleName == null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(72.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Image(
                        painter = painterResource(id = com.paraskcd.nitroless.R.drawable.banner),
                        contentDescription = "Logo",
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(248.dp),
                        contentScale = ContentScale.Fit,
                        alignment = Alignment.Center
                    )
                    IconButton(onClick = {
                        onButtonClicked()
                    }) {
                        Icon(Icons.Filled.Info, contentDescription = "")
                    }
                }
            } else {
                Text(text = titleName)
            }
        },
        navigationIcon = {
            IconButton(onClick = { onNavButtonClicked() } ) {
                Icon(buttonIcon, contentDescription = "")
            }
        },
        backgroundColor = MaterialTheme.colors.primaryVariant
    )
}