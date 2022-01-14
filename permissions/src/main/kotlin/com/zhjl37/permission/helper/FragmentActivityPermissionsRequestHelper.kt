package com.zhjl37.permission.helper

import android.content.Context
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

class FragmentActivityPermissionsRequestHelper(private val activity: FragmentActivity) :
    PermissionsRequestHelper() {

    override val context: Context
        get() = activity

    override fun checkPermission(permission: String): Int {
        return ContextCompat.checkSelfPermission(activity, permission)
    }

    override fun shouldShowRationale(permission: String): Boolean {
        return ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)
    }
}