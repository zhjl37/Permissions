package com.zhjl37.permission

import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.permissionx.guolindev.permissionMapOnQ
import com.permissionx.guolindev.permissionMapOnR
import com.permissionx.guolindev.permissionMapOnS

val getPermissionGroupForDefault = { context: Context, permission: String ->
    try {
        context.packageManager.getPermissionInfo(permission, 0).group
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
        null
    }
}
val getPermissionGroupForQ =
    { context: Context, permission: String -> permissionMapOnQ[permission] }
val getPermissionGroupForR =
    { context: Context, permission: String -> permissionMapOnR[permission] }
val getPermissionGroupForS =
    { context: Context, permission: String -> permissionMapOnS[permission] }

val getPermissionGroupFunctions = arrayOf(
    getPermissionGroupForDefault,
    getPermissionGroupForQ,
    getPermissionGroupForR,
    getPermissionGroupForS,
)

//----- FragmentActivity ------------------------------------------------------------------------

fun FragmentActivity.singlePermission(
    permission: String,
    options: Options? = null,
    callback: ActivityResultCallback<Boolean>
): SinglePermission {
    return SinglePermission(this, permission, options, callback)
}

fun FragmentActivity.singlePermission(
    permission: String,
    callback: ActivityResultCallback<Boolean>
): SinglePermission {
    return singlePermission(permission, null, callback)
}

fun FragmentActivity.multiplePermissions(
    permissions: Array<String>,
    options: Options? = null,
    callback: ActivityResultCallback<Map<String, Boolean>>
): MultiplePermissions {
    return MultiplePermissions(this, permissions, options, callback)
}

fun FragmentActivity.multiplePermissions(
    permissions: Array<String>,
    callback: ActivityResultCallback<Map<String, Boolean>>
): MultiplePermissions {
    return multiplePermissions(permissions, null, callback)
}

//----- Fragment --------------------------------------------------------------------------------

fun Fragment.singlePermission(
    permission: String,
    options: Options? = null,
    callback: ActivityResultCallback<Boolean>
): SinglePermission {
    return SinglePermission(this, permission, options, callback)
}

fun Fragment.singlePermission(
    permission: String,
    callback: ActivityResultCallback<Boolean>
): SinglePermission {
    return singlePermission(permission, null, callback)
}

fun Fragment.multiplePermissions(
    permissions: Array<String>,
    options: Options? = null,
    callback: ActivityResultCallback<Map<String, Boolean>>
): MultiplePermissions {
    return MultiplePermissions(this, permissions, options, callback)
}

fun Fragment.multiplePermissions(
    permissions: Array<String>,
    callback: ActivityResultCallback<Map<String, Boolean>>
): MultiplePermissions {
    return multiplePermissions(permissions, null, callback)
}
