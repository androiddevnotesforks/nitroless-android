@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")
package com.paraskcd.nitroless.keyboard

import android.app.Application
import android.content.Intent
import android.inputmethodservice.InputMethodService
import android.view.View
import androidx.annotation.CallSuper
import androidx.lifecycle.*
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.paraskcd.nitroless.Nitroless
import com.paraskcd.nitroless.repository.RepoRepository
import com.paraskcd.nitroless.viewmodel.RepoViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class IMEService:
    InputMethodService(),
    LifecycleOwner,
    ViewModelStoreOwner,
    SavedStateRegistryOwner {
    private val _lifecycleRegistry: LifecycleRegistry by lazy { LifecycleRegistry(this) }
    private val _store by lazy { ViewModelStore() }
    override fun getLifecycle(): Lifecycle = _lifecycleRegistry
    override fun getViewModelStore(): ViewModelStore = _store
    override val savedStateRegistry: SavedStateRegistry = SavedStateRegistryController.create(this).savedStateRegistry
    private fun handleLifecycleEvent(event: Lifecycle.Event) =
        _lifecycleRegistry.handleLifecycleEvent(event)

    @CallSuper
    override fun onCreate() {
        super.onCreate()
        // You must call performAttach() before calling performRestore(Bundle)
        savedStateRegistry.performAttach(lifecycle)
        savedStateRegistry.performRestore(null)
        handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
    }

    @CallSuper
    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        stopSelf()
    }

    @CallSuper
    override fun onDestroy() {
        super.onDestroy()
        handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    }

    @Inject
    lateinit var repoViewModel: RepoViewModel

    override fun onCreateInputView(): View {
        val view = ComposeKeyboardView(this)

        this.attachToDecorView(
            window?.window?.decorView
        )
        return view
    }

    fun attachToDecorView(decorView: View?) {
        if (decorView == null) return

        ViewTreeLifecycleOwner.set(decorView, this)
        ViewTreeViewModelStoreOwner.set(decorView, this)
        decorView.setViewTreeSavedStateRegistryOwner(this)
    }
}
