package com.paraskcd.nitroless.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.paraskcd.nitroless.ui.theme.AccentColor
import com.paraskcd.nitroless.ui.theme.Danger

@Composable
fun DeleteRepoPromptDialog(
    show: Boolean,
    deleteRepoOnClick: () -> Unit,
    cancelButtonOnClick: () -> Unit,
    repoName: String
) {
    AnimatedVisibility(
        visible = show,
        enter =  expandVertically() + fadeIn(),
        exit = shrinkVertically() + fadeOut()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xA5000000)),
            contentAlignment = Alignment.Center
        ) {
            DarkContainer {
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)) {
                    Text(
                        "Remove Repository - ${repoName}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight(700)
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = {
                                deleteRepoOnClick()
                            },
                            shape = CircleShape,
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = AccentColor,
                                contentColor = Color.White
                            ),
                            modifier = Modifier
                                .width(120.dp)
                                .padding(horizontal = 10.dp)
                        ) {
                            Text(text = "Remove")
                        }
                        Button(
                            onClick = {
                                cancelButtonOnClick()
                            },
                            shape = CircleShape,
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Danger,
                                contentColor = Color.White
                            ),
                            modifier = Modifier
                                .width(120.dp)
                                .padding(horizontal = 10.dp)
                        ) {
                            Text(text = "Cancel")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AddRepoPromptDialog(
    show: Boolean,
    addRepo: (String) -> Unit,
    repoToAdd: String,
    addRepoButtonOnClick: () -> Unit,
    cancelButtonOnClick: () -> Unit
) {
    AnimatedVisibility(
        visible = show,
        enter = expandVertically() + fadeIn(),
        exit = shrinkVertically() + fadeOut()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xA5000000)),
            contentAlignment = Alignment.Center
        ) {
            DarkContainer {
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)) {
                    Text(
                        "Add Repository",
                        fontSize = 20.sp,
                        fontWeight = FontWeight(700)
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    TextField(
                        value = repoToAdd,
                        onValueChange = { value ->
                            addRepo(value)
                        },
                        shape = CircleShape,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        colors = TextFieldDefaults.textFieldColors(
                            textColor = MaterialTheme.colors.onPrimary,
                            disabledTextColor = Color.Transparent,
                            backgroundColor = MaterialTheme.colors.secondary,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent
                        ),
                        placeholder = {
                            Text(text = "Repository URL")
                        }
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = {
                                addRepoButtonOnClick()
                            },
                            shape = CircleShape,
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = AccentColor,
                                contentColor = Color.White
                            ),
                            modifier = Modifier
                                .width(120.dp)
                                .padding(horizontal = 10.dp)
                        ) {
                            Text(text = "Add")
                        }
                        Button(
                            onClick = {
                                cancelButtonOnClick()
                            },
                            shape = CircleShape,
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Danger,
                                contentColor = Color.White
                            ),
                            modifier = Modifier
                                .width(120.dp)
                                .padding(horizontal = 10.dp)
                        ) {
                            Text(text = "Cancel")
                        }
                    }
                }
            }
        }
    }
}