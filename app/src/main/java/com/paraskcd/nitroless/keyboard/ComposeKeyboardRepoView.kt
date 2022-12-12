package com.paraskcd.nitroless.keyboard

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.AbstractComposeView
import androidx.compose.ui.unit.dp
import com.paraskcd.nitroless.ui.theme.BGPrimaryDarkColor
import com.paraskcd.nitroless.viewmodel.RepoViewModel

class ComposeKeyboardRepoView(context: Context): AbstractComposeView(context) {
    private val viewModel: RepoViewModel = (context as IMEService).repoViewModel

    @Composable
    override fun Content() {
        val repos = this.viewModel.repos.observeAsState().value
        val selectedRepo = this.viewModel.selectedRepo.observeAsState().value

        if (selectedRepo == null || !selectedRepo.selected) {
            (context as IMEService).setInputView(ComposeKeyboardView(context))
        }

        KeyboardRepoScreen(context = context, selectedRepo = selectedRepo, viewModel = viewModel, repos = repos)
    }
}