package com.paraskcd.nitroless.keyboard

import android.content.Context
import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.AbstractComposeView
import com.paraskcd.nitroless.model.FavouriteEmotesTable
import com.paraskcd.nitroless.model.FrequentlyUsedEmotesTable
import com.paraskcd.nitroless.model.Repo
import com.paraskcd.nitroless.model.RepoTable
import com.paraskcd.nitroless.repository.RepoRepository
import com.paraskcd.nitroless.viewmodel.RepoViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class ComposeKeyboardView(context: Context): AbstractComposeView(context) {
    private val viewModel: RepoViewModel = (context as IMEService).repoViewModel

    @Composable
    override fun Content() {
        val repos = this.viewModel.repos.observeAsState().value
        val frequentlyUsedEmotes = this.viewModel.frequentlyUsedEmotes.collectAsState().value

        val selectedRepo: Repo by remember {
            mutableStateOf(Repo(
                id = null,
                selected = false,
                url = null,
                author = null,
                description = null,
                emotes = emptyList(),
                icon = "",
                keywords = null,
                name = "",
                path = "",
                favouriteEmotes = null
            ))
        }

        KeyboardScreen(context, repos, frequentlyUsedEmotes, selectedRepo, this.viewModel)
    }
}