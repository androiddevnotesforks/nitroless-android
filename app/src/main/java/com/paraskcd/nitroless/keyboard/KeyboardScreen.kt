package com.paraskcd.nitroless.keyboard

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.paraskcd.nitroless.model.Repo
import com.paraskcd.nitroless.ui.theme.BGPrimaryDarkColor
import androidx.compose.ui.*
import com.paraskcd.nitroless.model.RepoTable

@Composable
fun KeyboardScreen() {
    Column(
        modifier = Modifier
            .background(BGPrimaryDarkColor)
            .fillMaxWidth()
            .height(360.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
    }
}
//
//@Composable
//fun FixedHeightBox(modifier: Modifier, height: Dp, content: @Composable () -> Unit) {
//    Layout(modifier = modifier, content = content) { measurables, constraints ->
//        val placeables = measurables.map { measurable ->
//            measurable.measure(constraints)
//        }
//        val h = height.roundToPx()
//        layout(constraints.maxWidth, h) {
//            placeables.forEach { placeable ->
//                placeable.place(x = 0, y = min(0,  h-placeable.height ))
//            }
//        }
//    }
//}
//
//
//@Composable
//fun KeyboardKey(
//    keyboardKey: String,
//    modifier: Modifier,
//) {
//    val context = LocalContext.current
//
//    val interactionSource = remember { MutableInteractionSource() }
//    val pressed = interactionSource.collectIsPressedAsState()
//    Box(modifier = modifier.fillMaxHeight(), contentAlignment = Alignment.BottomCenter) {
//        Text(keyboardKey, Modifier
//            .fillMaxWidth()
//            .padding(2.dp)
//            .border(1.dp, Color.Black)
//            .clickable(interactionSource = interactionSource, indication = null) {
//                (context as IMEService).currentInputConnection.commitText(keyboardKey, keyboardKey.length)
//            }
//            .background(Color.White)
//            .padding(
//                start = 12.dp,
//                end = 12.dp,
//                top = 16.dp,
//                bottom = 16.dp
//            )
//
//        )
//        if (pressed.value) {
//            Text(
//                keyboardKey, Modifier
//                    .fillMaxWidth()
//                    .border(1.dp, Color.Black)
//                    .background(Color.White)
//                    .padding(
//                        start = 16.dp,
//                        end = 16.dp,
//                        top = 16.dp,
//                        bottom = 48.dp
//                    )
//            )
//        }
//    }
//}