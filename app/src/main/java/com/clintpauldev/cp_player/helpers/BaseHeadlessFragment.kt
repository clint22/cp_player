package com.clintpauldev.cp_player.helpers

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CompletableDeferred

internal typealias PermissionResults = IntArray

open class BaseHeadlessFragment : Fragment() {

    protected val model: PermissionViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    protected fun exit() {
        retainInstance = false
        activity?.run {
            if (!isFinishing) supportFragmentManager
                .beginTransaction()
                .remove(this@BaseHeadlessFragment)
                .commitAllowingStateLoss()
        }

    }


}

class PermissionViewModel : ViewModel() {

    var permissionRationaleShown = false
    lateinit var deferredGrant: CompletableDeferred<Boolean>

    val permissionPending: Boolean
        get() = ::deferredGrant.isInitialized && !deferredGrant.isCompleted

    val isCompleted: Boolean
        get() = ::deferredGrant.isInitialized && deferredGrant.isCompleted

    fun complete(value: Boolean) {
        if (::deferredGrant.isInitialized) deferredGrant.complete(value)
    }

    fun setupDeferred() {
        deferredGrant = CompletableDeferred<Boolean>().apply {
            invokeOnCompletion { permissionRationaleShown = false }
        }
    }
}