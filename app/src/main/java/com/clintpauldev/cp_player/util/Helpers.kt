package com.clintpauldev.cp_player.util

import android.Manifest
import android.os.Build
import android.os.Environment

/**
 * Check if the app has the [Manifest.permission.MANAGE_EXTERNAL_STORAGE] granted
 */
fun isExternalStorageManager(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && Environment.isExternalStorageManager()