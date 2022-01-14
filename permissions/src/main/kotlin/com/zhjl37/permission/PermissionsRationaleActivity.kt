package com.zhjl37.permission

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.StyleSpan
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

open class PermissionsRationaleActivity : AppCompatActivity(),
    PermissionsRationaleFragment.OnClickListener {

    private var permissions0: Array<String>? = null
    private val permissions: Array<String>
        get() {
            if (permissions0 == null) {
                permissions0 = intent.getStringArrayExtra(EXTRA_PERMISSIONS)
            }
            return permissions0!!
        }

    private val launcher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(), ::onResult
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupDialog()
    }

    protected fun setupDialog() {
        val fragment = supportFragmentManager.findFragmentByTag(TAG_RATIONALE)
        if (fragment == null) {
            val title = getText(R.string.enable_permissions)
            val message = buildMessage()
            val positive = getText(R.string.settings)
            val negative = getText(R.string.not_now)

            PermissionsRationaleFragment.newInstance(permissions)
                .apply {
                    setTitle(title)
                    setMessage(message)
                    setPositive(positive)
                    setNegative(negative)
                }
                .show(supportFragmentManager, TAG_RATIONALE)
        }
    }

    protected fun buildMessage(): CharSequence {
        val appLabel = applicationInfo.loadLabel(packageManager)
        val message = getString(R.string.rationale_for_denied, appLabel.toString())
        return SpannableStringBuilder(message)
            .apply {
                setSpan(
                    StyleSpan(Typeface.BOLD),
                    0,
                    appLabel.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
    }

    override fun onClick(
        fragment: PermissionsRationaleFragment,
        dialog: DialogInterface,
        which: Int
    ) {
        when (which) {
            DialogInterface.BUTTON_POSITIVE -> launchApplicationDetailsSettings()
            DialogInterface.BUTTON_NEGATIVE -> cancel()
        }
    }

    protected fun launchApplicationDetailsSettings() {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        val applicationId = packageName
        val uri = Uri.fromParts("package", applicationId, null)
        intent.data = uri
        //intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        launcher.launch(intent)
    }

    private fun onResult(result: ActivityResult) {
        setResult(Activity.RESULT_OK, result.data)
        finish()
    }

    protected fun cancel() {
        setResult(Activity.RESULT_CANCELED)
        finish()
    }

    companion object {
        const val EXTRA_PERMISSIONS = "permissions"
        private const val TAG_RATIONALE = "rationale"
    }
}
