@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")
package com.paraskcd.nitroless.keyboard

import android.annotation.SuppressLint
import android.app.AppOpsManager
import android.content.*
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory
import android.inputmethodservice.InputMethodService
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.annotation.CallSuper
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.core.view.inputmethod.EditorInfoCompat
import androidx.core.view.inputmethod.InputConnectionCompat
import androidx.core.view.inputmethod.InputContentInfoCompat
import androidx.lifecycle.*
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.paraskcd.nitroless.viewmodel.RepoViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.IOException
import java.util.*
import javax.inject.Inject
import kotlin.concurrent.schedule


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
    private val TAG = "ImageKeyboard"
    val MIME_TYPE_GIF = "image/gif"
    val MIME_TYPE_PNG = "image/png"
    val MIME_TYPE_WEBP = "image/webp"
    private val mPngFile: File? = null
    private val mGifFile: File? = null
    private val mWebpFile: File? = null

    fun deleteBitmap(context: Context, imageFile: File) {
        // Set up the projection (we only need the ID)
        // Set up the projection (we only need the ID)
        val projection = arrayOf(MediaStore.Images.Media._ID)

        // Match on the file path

        // Match on the file path
        val selection = MediaStore.Images.Media.DATA + " = ?"
        val selectionArgs = arrayOf<String>(imageFile.getAbsolutePath())

        // Query for the ID of the media matching the file path

        // Query for the ID of the media matching the file path
        val queryUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val contentResolver: ContentResolver = context.contentResolver
        val c = contentResolver.query(queryUri, projection, selection, selectionArgs, null)

        if (c != null) {
            if (c.moveToFirst()) {
                // We found the ID. Deleting the item via the content provider will also remove the file
                val id = c.getLong(c.getColumnIndexOrThrow(MediaStore.Images.Media._ID))
                val deleteUri = ContentUris.withAppendedId(queryUri, id)
                contentResolver.delete(deleteUri, null, null)
            } else {
                // File not found in media store DB
            }
            c.close()
        }
    }

    @Throws(IOException::class)
    fun saveBitmap(
        context: Context, bitmap: Bitmap, format: CompressFormat,
        mimeType: String, displayName: String
    ): Uri {

        val values = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, displayName)
            put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DCIM)
        }

        val resolver = context.contentResolver
        var uri: Uri? = null

        try {
            uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                ?: throw IOException("Failed to create new MediaStore record.")

            resolver.openOutputStream(uri)?.use {
                if (!bitmap.compress(format, 100, it))
                    throw IOException("Failed to save bitmap.")
            } ?: throw IOException("Failed to open output stream.")

            return uri

        } catch (e: IOException) {

            uri?.let { orphanUri ->
                // Don't leave an orphan entry in the MediaStore
                resolver.delete(orphanUri, null, null)
            }

            throw e
        }
    }

    @SuppressLint("Range")
    private fun getLastImageId(context: Context): Int {
        val imageColumns = arrayOf(MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA)
        val imageOrderBy = MediaStore.Images.Media._ID + " DESC"
        val imageCursor: Cursor? = context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            imageColumns,
            null,
            null,
            imageOrderBy
        )
        return if (imageCursor != null && imageCursor.moveToFirst()) {
            val id: Int =
                imageCursor.getInt(imageCursor.getColumnIndex(MediaStore.Images.Media._ID))
            val fullPath: String =
                imageCursor.getString(imageCursor.getColumnIndex(MediaStore.Images.Media.DATA))
            Log.d(TAG, "getLastImageId::id $id")
            Log.d(TAG, "getLastImageId::path $fullPath")
            imageCursor.close()
            id
        } else {
            0
        }
    }

    private fun removeImage(context: Context, id: Int, uri: Uri) {
        val cr = context.contentResolver
        cr.delete(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            MediaStore.Images.Media._ID + "=?",
            arrayOf(java.lang.Long.toString(id.toLong()))
        )
        cr.delete(uri, null, null)
    }

    @Throws(java.lang.Exception::class)
    fun convertFileToContentUri(context: Context, file: File, format: CompressFormat, mimeType: String, description: String): Uri? {
        return saveBitmap(context, BitmapFactory.decodeFile(file.path), format = format, mimeType = mimeType, displayName = description)
    }

    private fun isCommitContentSupported(
        @Nullable editorInfo: EditorInfo?, @NonNull mimeType: String
    ): Boolean {
        if (editorInfo == null) {
            return false
        }
        val ic = currentInputConnection ?: return false
        if (!validatePackageName(editorInfo)) {
            return false
        }
        val supportedMimeTypes = EditorInfoCompat.getContentMimeTypes(editorInfo)
        for (supportedMimeType in supportedMimeTypes) {
            if (ClipDescription.compareMimeTypes(mimeType, supportedMimeType)) {
                return true
            }
        }
        return false
    }

    fun doCommitContent(
        @NonNull description: String, @NonNull mimeType: String,
        @NonNull file: File, @NonNull format: CompressFormat
    ) {
        val editorInfo = currentInputEditorInfo

        // Validate packageName again just in case.
        if (!validatePackageName(editorInfo)) {
            return
        }

        val contentUri: Uri =
            if (convertFileToContentUri(this, file, format, mimeType, description) != null ) {
                convertFileToContentUri(this, file, format, mimeType, description)!!
            } else {
                return
            }
        // As you as an IME author are most likely to have to implement your own content provider
        // to support CommitContent API, it is important to have a clear spec about what
        // applications are going to be allowed to access the content that your are going to share.
        val flag: Int
        if (Build.VERSION.SDK_INT >= 25) {
            // On API 25 and later devices, as an analogy of Intent.FLAG_GRANT_READ_URI_PERMISSION,
            // you can specify InputConnectionCompat.INPUT_CONTENT_GRANT_READ_URI_PERMISSION to give
            // a temporary read access to the recipient application without exporting your content
            // provider.
            flag = InputConnectionCompat.INPUT_CONTENT_GRANT_READ_URI_PERMISSION
        } else {
            // On API 24 and prior devices, we cannot rely on
            // InputConnectionCompat.INPUT_CONTENT_GRANT_READ_URI_PERMISSION. You as an IME author
            // need to decide what access control is needed (or not needed) for content URIs that
            // you are going to expose. This sample uses Context.grantUriPermission(), but you can
            // implement your own mechanism that satisfies your own requirements.
            flag = 0
            try {
                // TODO: Use revokeUriPermission to revoke as needed.
                grantUriPermission(
                    editorInfo.packageName, contentUri, Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            } catch (e: java.lang.Exception) {
                Log.e(
                    TAG, "grantUriPermission failed packageName=" + editorInfo.packageName
                            + " contentUri=" + contentUri, e
                )
            }
        }
        val inputContentInfoCompat = InputContentInfoCompat(
            contentUri,
            ClipDescription(description, arrayOf(mimeType)),
            null /* linkUrl */
        )
        InputConnectionCompat.commitContent(
            currentInputConnection, currentInputEditorInfo, inputContentInfoCompat,
            flag, null
        )
        Timer().schedule(2000) {
            this@IMEService.contentResolver.delete(contentUri, null, null)
        }
    }

    private fun validatePackageName(@Nullable editorInfo: EditorInfo?): Boolean {
        if (editorInfo == null) {
            return false
        }
        val packageName = editorInfo.packageName ?: return false

        // In Android L MR-1 and prior devices, EditorInfo.packageName is not a reliable identifier
        // of the target application because:
        //   1. the system does not verify it [1]
        //   2. InputMethodManager.startInputInner() had filled EditorInfo.packageName with
        //      view.getContext().getPackageName() [2]
        // [1]: https://android.googlesource.com/platform/frameworks/base/+/a0f3ad1b5aabe04d9eb1df8bad34124b826ab641
        // [2]: https://android.googlesource.com/platform/frameworks/base/+/02df328f0cd12f2af87ca96ecf5819c8a3470dc8
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return true
        }
        val inputBinding = currentInputBinding
        if (inputBinding == null) {
            // Due to b.android.com/225029, it is possible that getCurrentInputBinding() returns
            // null even after onStartInputView() is called.
            // TODO: Come up with a way to work around this bug....
            Log.e(
                TAG, "inputBinding should not be null here. "
                        + "You are likely to be hitting b.android.com/225029"
            )
            return false
        }
        val packageUid = inputBinding.uid
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val appOpsManager = getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
            try {
                appOpsManager.checkPackage(packageUid, packageName)
            } catch (e: Exception) {
                return false
            }
            return true
        }
        val packageManager = packageManager
        val possiblePackageNames = packageManager.getPackagesForUid(packageUid)
        for (possiblePackageName in possiblePackageNames!!) {
            if (packageName == possiblePackageName) {
                return true
            }
        }
        return false
    }

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
