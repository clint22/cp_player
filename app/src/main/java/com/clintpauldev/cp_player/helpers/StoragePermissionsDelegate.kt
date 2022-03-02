package com.clintpauldev.cp_player.helpers

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.UserDictionary.Words.APP_ID
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentActivity
import com.clintpauldev.cp_player.BuildConfig
import com.clintpauldev.cp_player.isCallable
import com.clintpauldev.cp_player.tools.INITIAL_PERMISSION_ASKED
import com.clintpauldev.cp_player.tools.Settings
import com.clintpauldev.cp_player.tools.putSingle
import com.clintpauldev.cp_player.util.LiveEvent
import com.clintpauldev.cp_player.util.Permissions
import com.clintpauldev.cp_player.util.isExternalStorageManager
import kotlinx.coroutines.ExperimentalCoroutinesApi


private const val WRITE_ACCESS = "write"
private const val WITH_DIALOG = "with_dialog"

@ExperimentalCoroutinesApi
class StoragePermissionsDelegate : BaseHeadlessFragment() {

    private var withDialog: Boolean = true
    private var timeAsked: Long = -1L
    private var askedPermission: Int = -1

    companion object {

        const val TAG = "CP/StorageHF"
        val storageAccessGranted = LiveEvent<Boolean>()

        suspend fun FragmentActivity.getStoragePermission(
            write: Boolean = false,
            withDialog: Boolean = true
        ): Boolean {
            if (isFinishing) return false
            Settings.getInstance(this).putSingle(INITIAL_PERMISSION_ASKED, true)
            val model: PermissionViewModel by viewModels()
            if (model.isCompleted && storageAccessGranted.value == true) return model.deferredGrant.getCompleted()
            if (model.permissionPending) {
                val fragment =
                    supportFragmentManager.findFragmentByTag(TAG) as? StoragePermissionsDelegate
                fragment?.requestStorageAccess(write) ?: return false
            } else {
                model.setupDeferred()
                val fragment = StoragePermissionsDelegate().apply {
                    arguments = bundleOf(WRITE_ACCESS to write, WITH_DIALOG to withDialog)
                }
                supportFragmentManager.beginTransaction().add(fragment, TAG)
                    .commitAllowingStateLoss()
            }
            return model.deferredGrant.await()
        }
    }


    private fun requestStorageAccess(write: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val uri = Uri.parse("package:${BuildConfig.APPLICATION_ID}")
            val intent =
                Intent(android.provider.Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri)
            if (intent.isCallable(requireActivity())) {
                if (withDialog) Permissions.showExternalPermissionDialog(requireActivity()) { asked ->
                    if (asked) {
                        askAllAccessPermission(intent)
                    }
                } else askAllAccessPermission(intent)
                return
            }
        }
        val code =
            if (write) Manifest.permission.WRITE_EXTERNAL_STORAGE else Manifest.permission.READ_EXTERNAL_STORAGE
        askedPermission =
            if (write) Permissions.PERMISSION_WRITE_STORAGE_TAG else Permissions.PERMISSION_STORAGE_TAG
        timeAsked = System.currentTimeMillis()
        activityResultLauncher.launch(code)
    }

    private fun askAllAccessPermission(intent: Intent) {
        val code = android.provider.Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
        askedPermission = Permissions.MANAGE_EXTERNAL_STORAGE
        timeAsked = System.currentTimeMillis()
        activityResultLauncher.launch(code)
        startActivity(intent)
    }


    private val activityResultLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            //Answered really quick (not human) -> forwarding to app settings
            if (activity == null) return@registerForActivityResult
            val delay = System.currentTimeMillis() - timeAsked
            if (delay < 300) {
                Permissions.showAppSettingsPage(requireActivity())
                return@registerForActivityResult
            }
            when (askedPermission) {
                Permissions.PERMISSION_STORAGE_TAG, Permissions.MANAGE_EXTERNAL_STORAGE -> {
                    // If request is cancelled, the result arrays are empty.
                    if (activity == null) return@registerForActivityResult
                    if (isGranted || isExternalStorageManager()) {
                        storageAccessGranted.value = true
                        model.complete(true)
                        exit()
                        return@registerForActivityResult
                    }
                    storageAccessGranted.value = false
                    if (model.permissionPending) model.deferredGrant.complete(false)
                    exit()
                }
                Permissions.PERMISSION_WRITE_STORAGE_TAG -> {
                    model.deferredGrant.complete(isGranted)
                    exit()
                }
            }
        }


}