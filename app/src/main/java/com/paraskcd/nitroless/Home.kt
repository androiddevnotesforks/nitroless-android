package com.paraskcd.nitroless

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.paraskcd.nitroless.ui.theme.NitrolessTheme

@Composable
fun Home(openDrawer: () -> Unit) {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(10.dp)
        .background(MaterialTheme.colors.primary)
    ) {
        Text(text = "Home")
    }
}

@Preview(showBackground = true)
@Composable
fun preview() {
    NitrolessTheme() {
        Home(openDrawer = { /* TODO */ })
    }
}
