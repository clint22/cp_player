package com.clintpauldev.cp_player.util

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.clintpauldev.cp_player.AppContextProvider
import com.clintpauldev.cp_player.R
import kotlinx.coroutines.NonCancellable.cancel

object Permissions {
    const val MANAGE_EXTERNAL_STORAGE = 256
    const val PERMISSION_STORAGE_TAG = 255
    const val PERMISSION_WRITE_STORAGE_TAG = 253

    var sAlertDialog: Dialog? = null

    fun canReadStorage(context: Context): Boolean {
        return !isMarshMallowOrLater || ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED || isExternalStorageManager()
    }

    /**
     * Display a dialog asking for the [Manifest.permission.MANAGE_EXTERNAL_STORAGE] permission if needed
     *
     * @param activity: the activity used to trigger the dialog
     * @param listener: the listener for the permission result
     */
    fun showExternalPermissionDialog(
        activity: FragmentActivity,
        listener: (boolean: Boolean) -> Unit
    ) {
        if (activity.isFinishing || sAlertDialog != null && sAlertDialog!!.isShowing) return
        sAlertDialog = createExternalManagerDialog(activity, listener)
    }

    /**
     * Display a dialog asking for the [Manifest.permission.MANAGE_EXTERNAL_STORAGE] permission
     *
     * @param activity: the activity used to trigger the dialog
     * @param listener: the listener for the permission result
     */
    private fun createExternalManagerDialog(
        activity: FragmentActivity,
        listener: (boolean: Boolean) -> Unit
    ): Dialog {
        val dialogBuilder = android.app.AlertDialog.Builder(activity)
            .setTitle(activity.getString(R.string.allow_storage_manager_title))
            .setMessage(
                activity.getString(
                    R.string.allow_storage_manager_description,
                    activity.getString(R.string.allow_storage_manager_explanation)
                )
            )
            .setIcon(R.drawable.ic_warning)
            .setPositiveButton(activity.getString(R.string.ok)) { _, _ ->
                listener.invoke(true)
            }
            .setNegativeButton(activity.getString(R.string.cancel)) { _, _ -> listener.invoke(false) }
            .setCancelable(false)
        return dialogBuilder.show().apply {
            if (activity is AppCompatActivity) activity.lifecycle.addObserver(object :
                LifecycleObserver {
                @Suppress("unused")
                @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
                fun clear() {
                    dismiss()
                }
            })
        }
    }

    fun showAppSettingsPage(activity: FragmentActivity) {
        val i = Intent()
        i.action = "android.settings.APPLICATION_DETAILS_SETTINGS"
        i.addCategory(Intent.CATEGORY_DEFAULT)
        i.data = "package:${AppContextProvider.appContext.packageName}".toUri()
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            activity.startActivity(i)
        } catch (ignored: Exception) {
        }

    }

}