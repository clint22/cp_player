package com.clintpauldev.cp_player

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo

/**
 * Checks if the intent is callable
 *
 * @param context: the context to use to test the intent
 * @return true if the intent is callable
 */
fun Intent.isCallable(context: Context): Boolean {
    val list: List<ResolveInfo> = context.packageManager.queryIntentActivities(
        this,
        PackageManager.MATCH_DEFAULT_ONLY
    )
    return list.isNotEmpty()
}