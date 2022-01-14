package com.zhjl37.permission

import android.content.Intent
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.zhjl37.permission.helper.FragmentActivityPermissionsRequestHelper
import com.zhjl37.permission.helper.FragmentPermissionsRequestHelper
import com.zhjl37.permission.helper.PermissionsRequestHelper
import java.util.*

class MultiplePermissions private constructor(
    private val permissions: Array<String>,
    private val options: Options?,
    private val callback: ActivityResultCallback<Map<String, Boolean>>,
    caller: ActivityResultCaller
) {
    private val launcher: ActivityResultLauncher<Array<String>>
    private val launcherForADS: ActivityResultLauncher<Intent>
    private lateinit var helper: PermissionsRequestHelper

    constructor(
        activity: FragmentActivity,
        permissions: Array<String>,
        options: Options? = null,
        callbacks: ActivityResultCallback<Map<String, Boolean>>
    ) : this(permissions, options, callbacks, activity) {
        helper = FragmentActivityPermissionsRequestHelper(activity)
    }

    constructor(
        fragment: Fragment,
        permissions: Array<String>,
        options: Options? = null,
        callbacks: ActivityResultCallback<Map<String, Boolean>>
    ) : this(permissions, options, callbacks, fragment) {
        helper = FragmentPermissionsRequestHelper(fragment)
    }

    fun granted(): Boolean {
        for (permission in permissions) {
            val result = helper.checkPermission(permission)
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    fun results(): Map<String, Boolean> {
        val results: MutableMap<String, Boolean> = HashMap()
        for (permission in permissions) {
            val result = helper.checkPermission(permission)
            results[permission] = result != PackageManager.PERMISSION_GRANTED
        }
        return results
    }

    fun shouldShowRationaleForDenied(): Boolean {
        for (permission in permissions) {
            val result = helper.checkPermission(permission)
            if (result == PackageManager.PERMISSION_DENIED) {
                val recordResult = helper.getRecordResult(permission)
                if (PermissionsRequestHelper.DENIED_AND_NEVER_ASK == recordResult
                    && !helper.shouldShowRationale(permission)
                ) {
                    return true
                }
            }
        }
        return false
    }

    fun request(): Boolean {
        if (granted()) {
            return true
        }

        if (shouldShowRationaleForDenied()) {
            if (callback is Callbacks) {
                val callbacks = callback as Callbacks
                callbacks.onShowRationaleForDenied()
            } else {
                val context = helper.context
                val clazz = options?.rationaleActivityClass
                    ?: Options.queryRationaleActivity(helper.context)
                val intent = Intent(context, clazz)
                intent.putExtra(PermissionsRationaleActivity.EXTRA_PERMISSIONS, permissions)
                launcherForADS.launch(intent)
            }
        } else {
            launcher.launch(permissions)
        }
        return false
    }

    private fun onResult(results: Map<String, Boolean>) {
        for ((permission, granted) in results) {
            if (java.lang.Boolean.TRUE == granted) {
                helper.record(permission, PermissionsRequestHelper.GRANTED)
            } else {
                if (helper.shouldShowRationale(permission)) {
                    helper.record(permission, PermissionsRequestHelper.DENIED)
                } else {
                    helper.record(permission, PermissionsRequestHelper.DENIED_AND_NEVER_ASK)
                }
            }
        }
        callback.onActivityResult(results)
    }

    private fun onAppDetailsSettingsResult(result: ActivityResult) {
        callback.onActivityResult(results())
    }

    interface Callbacks : ActivityResultCallback<Map<String?, Boolean?>?> {
        fun onShowRationaleForDenied()
    }

    init {
        launcher = caller.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions(),
            ::onResult
        )

        launcherForADS = caller.registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            ::onAppDetailsSettingsResult
        )
    }
}