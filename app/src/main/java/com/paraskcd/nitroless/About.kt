package com.paraskcd.nitroless

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.rounded.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun About() {
    LazyColumn(modifier = Modifier
        .fillMaxSize()
        .background(color = MaterialTheme.colors.background)
    ) {
        item { 
            Card(
                backgroundColor = MaterialTheme.colors.secondary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                border = BorderStroke(width = 1.dp, color = MaterialTheme.colors.primaryVariant.copy(alpha = 0.1F)),
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
                        Icon(Icons.Filled.Info, contentDescription = "")
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("About", fontSize = 20.sp, fontWeight = FontWeight(700))
                    }
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(text = "Nitroless is a small open source project made by students to help people without Nitro be able to use the community's Emotes to be used in discord. Nitroless is entirely community based requiring the community to make repositories where they can insert their own emotes and share them back to the community. The community uses this service by clicking/tapping on the image and it gets copied in their system's clipboard, allowing them to paste the Emote URL in Discord for the people to see.", fontSize = 15.sp)
                }
            }
        }
        item {
            Card(
                backgroundColor = MaterialTheme.colors.secondary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                border = BorderStroke(width = 1.dp, color = MaterialTheme.colors.primaryVariant.copy(alpha = 0.1F)),
                shape = RoundedCornerShape(20.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center
                ) {
                    Button(onClick = { /*TODO*/ }, colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent), elevation = ButtonDefaults.elevation(defaultElevation = 0.dp, pressedElevation = 0.dp, focusedElevation = 0.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(painter = painterResource(id = R.drawable.github_icon), contentDescription = "", modifier = Modifier.width(18.dp), contentScale = ContentScale.Fit)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(text = "Github", fontSize = 12.sp)
                        }
                    }
                    Button(onClick = { /*TODO*/ }, colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent), elevation = ButtonDefaults.elevation(defaultElevation = 0.dp, pressedElevation = 0.dp, focusedElevation = 0.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Rounded.Info, contentDescription = "", modifier = Modifier.width(18.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(text = "Website", fontSize = 12.sp)
                        }
                    }
                    Button(onClick = { /*TODO*/ }, colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent), elevation = ButtonDefaults.elevation(defaultElevation = 0.dp, pressedElevation = 0.dp, focusedElevation = 0.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(painter = painterResource(id = R.drawable.twitter_logo), contentDescription = "", modifier = Modifier.width(18.dp), contentScale = ContentScale.Fit)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(text = "Twitter", fontSize = 12.sp)
                        }
                    }
                }
            }
        }
        item {
            Card(
                backgroundColor = MaterialTheme.colors.secondary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                border = BorderStroke(width = 1.dp, color = MaterialTheme.colors.primaryVariant.copy(alpha = 0.1F)),
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
                        Icon(Icons.Filled.Info, contentDescription = "Credits")
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("Credits", fontSize = 20.sp, fontWeight = FontWeight(700))
                    }
                    Spacer(modifier = Modifier.height(5.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun previewAbout() {
    About()
}