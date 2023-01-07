package com.paraskcd.nitroless.keyboard

import android.content.Context
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.AbstractComposeView
import com.paraskcd.nitroless.Nitroless
import com.paraskcd.nitroless.ui.theme.*
import com.paraskcd.nitroless.viewmodel.RepoViewModel

class ComposeKeyboardStickersRepoView(context: Context): AbstractComposeView(context) {
    private val viewModel: RepoViewModel = (context as IMEService).repoViewModel
    private val preferences = (context.applicationContext as Nitroless).preferences

    @Composable
    override fun Content() {
        val repos = this.viewModel.repos.observeAsState().value
        val selectedRepo = this.viewModel.selectedRepo.observeAsState().value

        val hideFavouriteEmotes = remember { this.preferences?.getBoolean("hide-favourite-emotes-keyboard", false) }
        val hideFrequentlyUsedEmotes = remember { this.preferences?.getBoolean("hide-frequently-used-emotes-keyboard", false) }
        val hideRepositories = remember { this.preferences?.getBoolean("hide-repositories-keyboard", false) }
        val darkMode = remember { this.preferences?.getBoolean("dark-keyboard", true) }
        val systemDarkMode = remember { this.preferences?.getBoolean("system-dark-keyboard", false) }

        val textColor =
            if (systemDarkMode == true) {
                if (isSystemInDarkTheme()) {
                    TextDarkColor
                } else {
                    TextColor
                }
            } else {
                if (darkMode == true) {
                    TextDarkColor
                } else {
                    TextColor
                }
            }

        val bgPrimaryColor =
            if (systemDarkMode == true) {
                if (isSystemInDarkTheme()) {
                    BGPrimaryDarkColor
                } else {
                    BGPrimaryColor
                }
            } else {
                if (darkMode == true) {
                    BGPrimaryDarkColor
                } else {
                    BGPrimaryColor
                }
            }

        val bgSecondaryColor =
            if (systemDarkMode == true) {
                if (isSystemInDarkTheme()) {
                    BGSecondaryDarkColor
                } else {
                    BGSecondaryColor
                }
            } else {
                if (darkMode == true) {
                    BGSecondaryDarkColor
                } else {
                    BGSecondaryColor
                }
            }

        val bgTertiaryColor =
            if (systemDarkMode == true) {
                if (isSystemInDarkTheme()) {
                    BGTertiaryDarkColor
                } else {
                    BGTertiaryColor
                }
            } else {
                if (darkMode == true) {
                    BGTertiaryDarkColor
                } else {
                    BGTertiaryColor
                }
            }

        val history_icon =
            if (systemDarkMode == true) {
                if (isSystemInDarkTheme()) {
                    com.paraskcd.nitroless.R.drawable.history_icon_keyboard
                } else {
                    com.paraskcd.nitroless.R.drawable.history_dark_icon_keyboard
                }
            } else {
                if (darkMode == true) {
                    com.paraskcd.nitroless.R.drawable.history_icon_keyboard
                } else {
                    com.paraskcd.nitroless.R.drawable.history_dark_icon_keyboard
                }
            }

        val backspace_icon =
            if (systemDarkMode == true) {
                if (isSystemInDarkTheme()) {
                    com.paraskcd.nitroless.R.drawable.backspace
                } else {
                    com.paraskcd.nitroless.R.drawable.backspace_dark
                }
            } else {
                if (darkMode == true) {
                    com.paraskcd.nitroless.R.drawable.backspace
                } else {
                    com.paraskcd.nitroless.R.drawable.backspace_dark
                }
            }

        if (selectedRepo == null || !selectedRepo.selected) {
            (context as IMEService).setInputView(ComposeKeyboardStickersView(context))
        }

        KeyboardStickersRepoScreen(
            context = context,
            selectedRepo = selectedRepo!!,
            viewModel = viewModel,
            repos = repos,
            textColor = textColor,
            bgPrimaryColor = bgPrimaryColor,
            bgSecondaryColor = bgSecondaryColor,
            bgTertiaryColor = bgTertiaryColor,
            backspace_icon = backspace_icon,
            hideRepositories = hideRepositories!!,
            hideFavouriteEmotes = hideFavouriteEmotes!!
        )
    }
}