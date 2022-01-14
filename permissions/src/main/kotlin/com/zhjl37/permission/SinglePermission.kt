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

class SinglePermission private constructor(
    private val permission: String,
    private val options: Options?,
    private val callback: ActivityResultCallback<Boolean>,
    caller: ActivityResultCaller
) {
    private val launcher: ActivityResultLauncher<String>
    private val launcherForADS: ActivityResultLauncher<Intent>
    private lateinit var helper: PermissionsRequestHelper

    constructor(
        activity: FragmentActivity,
        permission: String,
        options: Options? = null,
        callbacks: ActivityResultCallback<Boolean>
    ) : this(permission, options, callbacks, activity) {
        helper = FragmentActivityPermissionsRequestHelper(activity)
    }

    constructor(
        fragment: Fragment,
        permission: String,
        options: Options? = null,
        callbacks: ActivityResultCallback<Boolean>
    ) : this(permission, options, callbacks, fragment) {
        helper = FragmentPermissionsRequestHelper(fragment)
    }

    fun granted(): Boolean {
        return helper.checkPermission(permission) == PackageManager.PERMISSION_GRANTED
    }

    fun shouldShowRationaleForDenied(): Boolean {
        val result = helper.checkPermission(permission)
        if (result == PackageManager.PERMISSION_DENIED) {
            val recordResult = helper.getRecordResult(permission)
            return PermissionsRequestHelper.DENIED_AND_NEVER_ASK == recordResult
                    && !helper.shouldShowRationale(permission)
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
                intent.putExtra(
                    PermissionsRationaleActivity.EXTRA_PERMISSIONS,
                    arrayOf(permission)
                )
                launcherForADS.launch(intent)
            }
        } else {
            launcher.launch(permission)
        }
        return false
    }

    private fun onResult(result: Boolean) {
        if (java.lang.Boolean.TRUE == result) {
            helper.record(permission, PermissionsRequestHelper.GRANTED)
        } else {
            if (helper.shouldShowRationale(permission)) {
                helper.record(permission, PermissionsRequestHelper.DENIED)
            } else {
                helper.record(permission, PermissionsRequestHelper.DENIED_AND_NEVER_ASK)
            }
        }
        callback.onActivityResult(result)
    }

    private fun onAppDetailsSettingsResult(result: ActivityResult) {
        callback.onActivityResult(granted())
    }

    interface Callbacks : ActivityResultCallback<Boolean?> {
        fun onShowRationaleForDenied()
    }

    init {
        launcher = caller.registerForActivityResult(
            ActivityResultContracts.RequestPermission(),
            ::onResult
        )

        launcherForADS = caller.registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            ::onAppDetailsSettingsResult
        )
    }
}
