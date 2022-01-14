package com.zhjl37.permission.samples

import android.Manifest
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.zhjl37.permission.Options
import com.zhjl37.permission.PermissionsRationaleActivity
import com.zhjl37.permission.multiplePermissions
import com.zhjl37.permission.samples.databinding.SampleBinding

class Sample : AppCompatActivity() {

    companion object {
        private val PERMISSIONS_REQUIRED = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        private val PERMISSIONS_OPTIONS = Options().apply {
            rationaleActivityClass = PermissionsRationaleActivity::class.java
        }
    }

    private lateinit var binding: SampleBinding

    private val permissions = multiplePermissions(
        permissions = PERMISSIONS_REQUIRED,
        options = PERMISSIONS_OPTIONS,
        callback = ::onActivityResult
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = SampleBinding
            .inflate(layoutInflater)
            .apply {
                button1.setOnClickListener(::onClick)
            }

        setContentView(binding.root)
        updateViews()
    }

    private fun updateViews() {
        if (permissions.granted()) {
            binding.text1.visibility = View.VISIBLE
            binding.button1.visibility = View.GONE
            binding.image.setImageResource(R.drawable.undraw_confirmed_re_sef7)
            return
        }

        binding.text1.visibility = View.GONE
        binding.button1.visibility = View.VISIBLE

        if (permissions.shouldShowRationaleForDenied()) {
            binding.image.setImageResource(R.drawable.undraw_app_data_re_vg5c)
        } else {
            binding.image.setImageResource(R.drawable.undraw_accept_request_re_d81h)
        }
    }

    private fun onClick(view: View) {
        when (view.id) {
            android.R.id.button1 -> {
                if (permissions.request()) {
                    Toast.makeText(this, R.string.prompt_permissions_granted, Toast.LENGTH_SHORT)
                        .show()
                    return
                }
            }
        }
    }

    private fun onActivityResult(result: Map<String, Boolean>) {
        updateViews()
    }
}
