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
        val favouriteEmotes = this.viewModel.favouriteEmotes.collectAsState().value
        val selectedRepo = this.viewModel.selectedRepo.observeAsState().value

        if (selectedRepo != null && selectedRepo.selected) {
            (context as IMEService).setInputView(ComposeKeyboardRepoView(context))
        }

        KeyboardScreen(context, repos, frequentlyUsedEmotes, this.viewModel, favouriteEmotes)
    }
}