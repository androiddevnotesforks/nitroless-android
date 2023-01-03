package com.paraskcd.nitroless.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.paraskcd.nitroless.ui.theme.AccentColor

@Composable
fun Picker(repoMenu: Int, onClickRepoMenu: (Int) -> Unit) {
    TabRow(
        selectedTabIndex = repoMenu,
        backgroundColor = MaterialTheme.colors.primaryVariant,
        modifier = Modifier
            .padding(vertical = 10.dp, horizontal = 40.dp)
            .shadow(elevation = 10.dp, shape = CircleShape)
            .clip(CircleShape),
        indicator = { tabPositions: List<TabPosition> ->
            Box {}
        },
        divider = @Composable {
            Spacer(modifier = Modifier)
        }
    ) {
        val list = listOf("Emotes", "Stickers")
        list.forEachIndexed { index, text ->
            val selected = repoMenu == index
            Tab(
                selected = selected,
                onClick = { onClickRepoMenu(index) },
                modifier =
                if (selected) {
                    Modifier
                        .clip(CircleShape)
                        .background(AccentColor)
                } else {
                    Modifier
                        .clip(CircleShape)
                        .background(MaterialTheme.colors.primaryVariant)
                },
                text = { Text(text = text, color = MaterialTheme.colors.onPrimary) }
            )
        }
    }
}