package com.paraskcd.nitroless.elements

import android.graphics.drawable.Icon
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Container(content: @Composable () -> Unit) {
    Card(
        backgroundColor = MaterialTheme.colors.secondary,
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colors.primaryVariant.copy(alpha = 0.1F)),
        shape = RoundedCornerShape(20.dp)
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
            .padding(10.dp),
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colors.primaryVariant.copy(alpha = 0.1F)),
        shape = RoundedCornerShape(20.dp)
    ) {
        content()
    }
}
