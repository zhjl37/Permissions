package com.zhjl37.permission.helper

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

class FragmentPermissionsRequestHelper(private val fragment: Fragment) :
    PermissionsRequestHelper() {

    override val context: Context
        get() = fragment.requireContext()

    override fun checkPermission(permission: String): Int {
        return ContextCompat.checkSelfPermission(context, permission)
    }

    override fun shouldShowRationale(permission: String): Boolean {
        return fragment.shouldShowRequestPermissionRationale(permission)
    }
}