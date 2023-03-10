package com.paraskcd.nitroless

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.res.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.*
import androidx.navigation.NavHostController
import com.paraskcd.nitroless.components.Container
import com.paraskcd.nitroless.components.TopBar
import com.paraskcd.nitroless.utils.NetworkImage

@Composable
fun About(navController: NavHostController) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(color = MaterialTheme.colors.primary)
    ) {
        TopBar(
            titleName = "About",
            buttonIcon = Icons.Filled.ArrowBack,
            onNavButtonClicked = {
                navController.popBackStack()
            },
            onInfoButtonClicked = {},
            onSettingsButtonClicked = {}
        )
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
                            Icon(Icons.Filled.Info, contentDescription = "")
                            Spacer(modifier = Modifier.width(10.dp))
                            Text("About", fontSize = 20.sp, fontWeight = FontWeight(700))
                        }
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(text = "Nitroless is a small open-sourced project made by students to let those without Nitro use custom emojis on Discord. Nitroless is entirely community driven as users are able to create and host repositories that store static and animated emojis. These repositories can be shared and added across all platforms that Nitroless supports, much like how you join Discord servers. You can start using Nitroless either through the app by tapping on an emoji and pasting in chat, or through the keyboard for a seamless experience on Discord. Happy chatting!", fontSize = 15.sp)
                    }
                }
            }
            item {
                Container {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            onClick = {
                                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Nitroless")))
                            },
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
                            elevation = ButtonDefaults.elevation(defaultElevation = 0.dp, pressedElevation = 0.dp, focusedElevation = 0.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(painter = painterResource(id = R.drawable.github_icon), contentDescription = "", modifier = Modifier.width(18.dp), contentScale = ContentScale.Fit)
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(text = "Github", fontSize = 12.sp)
                            }
                        }
                        Button(
                            onClick = {
                                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://nitroless.app")))
                            },
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
                            elevation = ButtonDefaults.elevation(defaultElevation = 0.dp, pressedElevation = 0.dp, focusedElevation = 0.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Rounded.Info, contentDescription = "", modifier = Modifier.width(18.dp))
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(text = "Website", fontSize = 12.sp)
                            }
                        }
                        Button(
                            onClick = {
                                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/nitroless_")))
                            },
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
                            elevation = ButtonDefaults.elevation(defaultElevation = 0.dp, pressedElevation = 0.dp, focusedElevation = 0.dp)
                        ) {
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
                Container {
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
                        CreditBuilder(
                            imageURL = "https://github.com/TheAlphaStream.png",
                            imageDescription = "TheAlphaStream Profile Image",
                            name = "Alpha_Stream",
                            byLine = "Founder and Designer",
                            portfolioURL = "https://alphastream.weebly.com/",
                            twitterURL = "https://twitter.com/Kutarin_/",
                            twitterProfileName = "@Kutarin_",
                            githubURL = "https://github.com/TheAlphaStream/",
                            githubProfileName = "TheAlphaStream"
                        )
                        CreditBuilder(
                            imageURL = "https://github.com/paraskcd1315.png",
                            imageDescription = "ParasKCD Profile Image",
                            name = "Paras KCD",
                            byLine = "Android, Web, Electron, iOS and macOS Developer",
                            portfolioURL = "https://paraskcd.com/",
                            twitterURL = "https://twitter.com/ParasKCD/",
                            twitterProfileName = "@ParasKCD",
                            githubURL = "https://github.com/paraskcd1315/",
                            githubProfileName = "paraskcd1315"
                        )
                        CreditBuilder(
                            imageURL = "https://github.com/llsc12.png",
                            imageDescription = "llsc12 Profile Image",
                            name = "LLSC12",
                            byLine = "iOS and macOS Developer",
                            portfolioURL = "https://llsc12.me/",
                            twitterURL = "https://twitter.com/llsc121",
                            twitterProfileName = "@llsc121",
                            githubURL = "https://github.com/llsc12/",
                            githubProfileName = "llsc12"
                        )
                        CreditBuilder(
                            imageURL = "https://github.com/Superbro9.png",
                            imageDescription = "Superbro9 Profile Image",
                            name = "Superbro",
                            byLine = "iOS and macOS Adviser, Quality Control",
                            portfolioURL = null,
                            twitterURL = "https://twitter.com/suuperbro/",
                            twitterProfileName = "@suuperbro",
                            githubURL = "https://github.com/Superbro9/",
                            githubProfileName = "Superbro9"
                        )
                    }
                }
            }
        }
    }

}

@Composable
fun CreditBuilder(
    imageURL: String,
    imageDescription: String = "",
    name: String,
    byLine: String,
    portfolioURL: String?,
    twitterURL: String = "",
    twitterProfileName: String = "",
    githubURL: String = "",
    githubProfileName: String = ""
) {
    val context = LocalContext.current

    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(10.dp), verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.Start) {
        NetworkImage(
            imageURL = imageURL,
            imageDescription = imageDescription,
            size = 64.dp,
            shape = CircleShape
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(text = name, fontSize = 18.sp, fontWeight = FontWeight(700))
            Text(text = byLine, fontSize = 12.sp, fontWeight = FontWeight(400))
            Spacer(modifier = Modifier.height(5.dp))
            if (portfolioURL != null) {
                Button(
                    onClick = {
                        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(portfolioURL)))
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
                    elevation = ButtonDefaults.elevation(
                        defaultElevation = 0.dp,
                        pressedElevation = 0.dp,
                        focusedElevation = 0.dp
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Rounded.Info,
                            contentDescription = "",
                            modifier = Modifier.width(18.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(text = "Portfolio", fontSize = 12.sp)
                    }
                }
            }
            Button(
                onClick = {
                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(twitterURL)))
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
                elevation = ButtonDefaults.elevation(defaultElevation = 0.dp, pressedElevation = 0.dp, focusedElevation = 0.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(painter = painterResource(id = R.drawable.twitter_logo), contentDescription = "", modifier = Modifier.width(18.dp), contentScale = ContentScale.Fit)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = twitterProfileName, fontSize = 12.sp)
                }
            }
            Button(
                onClick = {
                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(githubURL)))
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
                elevation = ButtonDefaults.elevation(defaultElevation = 0.dp, pressedElevation = 0.dp, focusedElevation = 0.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(painter = painterResource(id = R.drawable.github_icon), contentDescription = "", modifier = Modifier.width(18.dp), contentScale = ContentScale.Fit)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = githubProfileName, fontSize = 12.sp)
                }
            }
        }
    }
}