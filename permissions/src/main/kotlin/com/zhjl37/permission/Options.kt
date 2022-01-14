package com.zhjl37.permission

import android.app.Activity
import android.content.Context
import android.content.Intent

class Options {
    lateinit var rationaleActivityClass: Class<out Activity>

    companion object {
        @Suppress("UNCHECKED_CAST")
        internal val queryRationaleActivity by lazy {
            { context: Context ->
                val packageName = context.packageName
                val intent = Intent("$packageName.intent.action.PERMISSIONS_RATIONALE")

                var clazz: Class<out Any>? = null
                val list = context.packageManager.queryIntentActivities(intent, 0)
                val len = list.size
                for (i in 0 until len) {
                    val activityInfo = list[i].activityInfo
                    if (context.packageName.equals(activityInfo.applicationInfo.packageName)) {
                        clazz = Class.forName(activityInfo.name)
                        break
                    }
                }

                if (null != clazz) {
                    clazz as Class<out Activity>
                } else {
                    PermissionsRationaleActivity::class.java
                }
            }
        }
    }
}