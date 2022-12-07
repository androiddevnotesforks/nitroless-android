package com.paraskcd.nitroless.keyboard

import android.content.Context
import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.AbstractComposeView
import androidx.hilt.navigation.compose.hiltViewModel
import com.paraskcd.nitroless.model.Repo
import com.paraskcd.nitroless.viewmodel.RepoViewModel

class ComposeKeyboardView(context: Context): AbstractComposeView(context) {
    @Composable
    override fun Content() {
        val viewModel: RepoViewModel = (context as IMEService).viewModel

        KeyboardScreen()
    }
}