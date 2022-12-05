package com.paraskcd.nitroless.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Container(content: @Composable () -> Unit) {
    Card(
        backgroundColor = MaterialTheme.colors.secondary,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
            .padding(bottom = 5.dp)
            .padding(horizontal = 10.dp),
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colors.primaryVariant.copy(alpha = 0.1F)),
        shape = RoundedCornerShape(20.dp)
    ) {
        content()
    }
}

@Composable
fun ContainerPill(content: @Composable () -> Unit) {
    Card(
        backgroundColor = MaterialTheme.colors.secondary,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
            .padding(bottom = 5.dp)
            .padding(horizontal = 10.dp),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colors.primaryVariant.copy(alpha = 0.1F)
        ),
        shape = CircleShape
    ) {
        content()
    }
}

@Composable
fun DarkContainer(content: @Composable () -> Unit) {
    Card(
        backgroundColor = MaterialTheme.colors.secondary,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
            .padding(bottom = 5.dp)
            .padding(horizontal = 10.dp),
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colors.primaryVariant.copy(alpha = 0.1F)),
        shape = RoundedCornerShape(20.dp)
    ) {
        content()
    }
}
