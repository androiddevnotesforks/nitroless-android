package com.paraskcd.nitroless.screens

import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.paraskcd.nitroless.components.Container
import com.paraskcd.nitroless.components.TopBar
import com.paraskcd.nitroless.ui.theme.AccentColor
import splitties.systemservices.inputMethodManager

@Composable
fun Settings(navController: NavHostController) {
    val context = LocalContext.current
    val (text, setValue) = remember { mutableStateOf(TextFieldValue("")) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(color = MaterialTheme.colors.primary)
    ) {
        TopBar(
            titleName = "Keyboard Settings",
            buttonIcon = Icons.Filled.ArrowBack,
            onNavButtonClicked = {
                navController.popBackStack()
            },
            onInfoButtonClicked = {},
            onSettingsButtonClicked = {}
        )
        LazyColumn() {
            item {
                Container {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Button(
                            onClick = {
                                context.startActivity(Intent(Settings.ACTION_INPUT_METHOD_SETTINGS))
                            },
                            shape = CircleShape,
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = AccentColor,
                                contentColor = Color.White
                            ),
                            modifier = Modifier
                                .width(300.dp)
                                .padding(horizontal = 10.dp)
                        ) {
                            Text(text = "Enable Nitroless Keyboard")
                        }
                        Button(
                            onClick = {
                                inputMethodManager.showInputMethodPicker()
                            },
                            shape = CircleShape,
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = AccentColor,
                                contentColor = Color.White
                            ),
                            modifier = Modifier
                                .width(300.dp)
                                .padding(horizontal = 10.dp)
                        ) {
                            Text(text = "Select Nitroless Keyboard")
                        }
                    }
                }
            }
            item {
                Container {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    ) {
                        TextField(
                            value = text,
                            onValueChange = { value ->
                                setValue(value)
                            },
                            shape = CircleShape,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            colors = TextFieldDefaults.textFieldColors(
                                textColor = MaterialTheme.colors.onPrimary,
                                disabledTextColor = Color.Transparent,
                                backgroundColor = MaterialTheme.colors.primaryVariant,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent
                            ),
                            placeholder = {
                                Text(text = "Try the Nitroless Keyboard")
                            }
                        )
                    }
                }
            }
            item {
                Container {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Filled.Info, contentDescription = "")
                            Spacer(modifier = Modifier.width(10.dp))
                            Text("Information", fontSize = 20.sp, fontWeight = FontWeight(700))
                        }
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(text = "The Nitroless App also comes with a keyboard which you can activate it to use all the emotes saved anywhere beyond the app.", fontSize = 15.sp)
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(text = "To activate the Keyboard you'll need to go Language and Input settings and enable the keyboard from there. To make it easier, the app provides a shortcut to go to that setting directly, just tap the Enable Nitroless Keyboard Button above.", fontSize = 15.sp)
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(text = "After enabling it, you can easily change keyboards via the navigation buttons in the Android System. As soon as you open a keyboard, you'll notice a small button Icon as a keyboard near the navigation buttons. Tapping on that keyboard icon, opens up a dialog where you can choose to switch keyboards.", fontSize = 15.sp)
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(text = "To make it easier, the app also provides a shortcut to go to that setting directly, just tap the Select Nitroless Keyboard Button above.", fontSize = 15.sp)
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(text = "It is recommended to not disable any other keyboard you use to type text as this keyboard doesn't provide any type of Text Input, just emotes.", fontSize = 15.sp)
                    }
                }
            }
        }
    }
}