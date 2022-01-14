package com.zhjl37.permission.helper

import android.content.Context
import android.content.SharedPreferences

abstract class PermissionsRequestHelper {
    private var preferences: SharedPreferences? = null
    abstract val context: Context
    abstract fun checkPermission(permission: String): Int
    abstract fun shouldShowRationale(permission: String): Boolean

    private fun ensurePreferences() {
        if (preferences == null) {
            preferences = context.getSharedPreferences("permissions", Context.MODE_PRIVATE)
        }
    }

    fun record(permission: String?, result: String?) {
        ensurePreferences()
        preferences!!.edit().putString(permission, result).apply()
    }

    fun getRecordResult(permission: String?): String? {
        ensurePreferences()
        return preferences!!.getString(permission, null)
    }

    companion object {
        const val GRANTED = "granted"
        const val DENIED = "denied"
        const val DENIED_AND_NEVER_ASK = "denied_and_never_ask"
    }
}