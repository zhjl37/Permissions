package com.zhjl37.permission

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zhjl37.permission.databinding.PermissionItemBinding

class PermissionsRationaleFragment : AppCompatDialogFragment(),
    DialogInterface.OnClickListener {

    private var title: CharSequence? = null
    private var message: CharSequence? = null
    private var positive: CharSequence? = null
    private var negative: CharSequence? = null

    private var permissions0: Array<String>? = null
    private val permissions: Array<String>
        get() {
            if (permissions0 == null) {
                permissions0 = requireArguments().getStringArray(ARG_PERMISSIONS)
            }
            return permissions0!!
        }

    private lateinit var permissionAdapter: PermissionAdapter

    private var onClickListener: OnClickListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setListeners(parentFragment)
        setListeners(activity)
    }

    private fun setListeners(obj: Any?) {
        if (onClickListener == null && obj is OnClickListener) {
            onClickListener = obj
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.run {
            title = getCharSequence(SAVED_TITLE)
            message = getCharSequence(SAVED_MESSAGE)
            positive = getCharSequence(SAVED_POSITIVE)
            negative = getCharSequence(SAVED_NEGATIVE)
        }

        isCancelable = false
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val context = requireContext();
        val permissionList = ArrayList<String>()
        permissions.forEach {
            if (ContextCompat.checkSelfPermission(context, it)
                != PackageManager.PERMISSION_GRANTED
            ) {
                permissionList.add(it)
            }
        }
        permissionAdapter = PermissionAdapter(permissionList)

        val recyclerViewPadding = recyclerViewPadding()
        val recyclerView = RecyclerView(context)
            .apply {
                isNestedScrollingEnabled = false
                isFocusable = false
                isFocusableInTouchMode = false
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                ViewCompat.setPaddingRelative(
                    this,
                    recyclerViewPadding[0],
                    recyclerViewPadding[1],
                    recyclerViewPadding[2],
                    recyclerViewPadding[3],
                )

                adapter = permissionAdapter
            }

        return AlertDialog.Builder(context, theme)
            .setView(recyclerView)
            .create()
    }

    private fun recyclerViewPadding(): IntArray {
        val attrs = intArrayOf(R.attr.dialogPreferredPadding)
        val typedArray = requireContext().theme.obtainStyledAttributes(attrs)
        val dialogPreferredPadding = typedArray.getDimensionPixelOffset(0, 0)
        typedArray.recycle()

        val paddingHorizontal = dialogPreferredPadding
        val paddingVertical = resources.getDimensionPixelOffset(R.dimen._8sdp)
        return intArrayOf(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical)
    }

    override fun onGetLayoutInflater(savedInstanceState: Bundle?): LayoutInflater {
        val layoutInflater: LayoutInflater = super.onGetLayoutInflater(savedInstanceState)
        if (showsDialog) {
            (dialog as? AlertDialog)?.apply {
                setTitle(title)
                setMessage(message)
                setButton(
                    DialogInterface.BUTTON_POSITIVE,
                    positive,
                    this@PermissionsRationaleFragment
                )
                setButton(
                    DialogInterface.BUTTON_NEGATIVE,
                    negative,
                    this@PermissionsRationaleFragment
                )
            }
        }
        return layoutInflater
    }

    override fun onClick(dialog: DialogInterface, which: Int) {
        onClickListener?.onClick(this, dialog, which)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.run {
            putCharSequence(SAVED_TITLE, title)
            putCharSequence(SAVED_MESSAGE, message)
            putCharSequence(SAVED_POSITIVE, positive)
            putCharSequence(SAVED_NEGATIVE, negative)
        }
    }

    fun setTitle(title: CharSequence?) {
        this.title = title
        (dialog as? AlertDialog)?.setTitle(title)
    }

    fun setMessage(message: CharSequence?) {
        this.message = message
        (dialog as? AlertDialog)?.setMessage(message)
    }

    fun setPositive(positive: CharSequence?) {
        this.positive = positive
        (dialog as? AlertDialog)
            ?.setButton(DialogInterface.BUTTON_POSITIVE, positive, this)
    }

    fun setNegative(negative: CharSequence?) {
        this.negative = negative
        (dialog as? AlertDialog)
            ?.setButton(DialogInterface.BUTTON_NEGATIVE, negative, this)
    }

    private class PermissionAdapter(private val permissions: ArrayList<String>) :
        RecyclerView.Adapter<PermissionAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder.create(parent)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(permissions[position])
        }

        override fun getItemCount(): Int {
            return permissions.size
        }

        class ViewHolder(private val binding: PermissionItemBinding) :
            RecyclerView.ViewHolder(binding.root) {
            companion object {
                fun create(viewGroup: ViewGroup): ViewHolder {
                    val binding = PermissionItemBinding
                        .inflate(
                            LayoutInflater.from(viewGroup.context), viewGroup, false
                        )
                    return ViewHolder(binding)
                }
            }

            fun bind(permission: String) {
                val iconRes = permissionIcon(permission)
                val labelRes = permissionLabel(permission)

                if (iconRes != 0) {
                    binding.icon.setImageResource(iconRes)
                } else {
                    binding.icon.setImageDrawable(null)
                }

                if (labelRes != 0) {
                    binding.text1.setText(labelRes)
                } else {
                    binding.text1.text = ""
                }
            }

            fun permissionGroup(permission: String): String? {
                val context = itemView.context
                for (getPermissionGroupFunction in getPermissionGroupFunctions) {
                    val permissionGroup = getPermissionGroupFunction(context, permission)
                    if (permissionGroup != null
                        && permissionGroup != "android.permission-group.UNDEFINED"
                    ) {
                        return permissionGroup
                    }
                }
                return null
            }

            fun permissionLabel(permission: String): Int {
                val permissionGroup = permissionGroup(permission)
                if (permissionGroup != null) {
                    return itemView.context.packageManager
                        .getPermissionGroupInfo(permissionGroup, 0).labelRes

                }
                return 0
            }

            fun permissionIcon(permission: String): Int {
                val permissionGroup = permissionGroup(permission)
                if (permissionGroup != null) {
                    return itemView.context.packageManager
                        .getPermissionGroupInfo(permissionGroup, 0).icon
                }
                return 0
            }
        }
    }

    interface OnClickListener {
        fun onClick(
            fragment: PermissionsRationaleFragment,
            dialog: DialogInterface,
            which: Int
        )
    }

    companion object {
        fun newInstance(permissions: Array<String>): PermissionsRationaleFragment {
            val args = Bundle()
            args.putStringArray(ARG_PERMISSIONS, permissions)

            return PermissionsRationaleFragment().apply {
                arguments = args
            }
        }

        const val ARG_PERMISSIONS = "permissions"

        private const val SAVED_TITLE = "title"
        private const val SAVED_MESSAGE = "message"
        private const val SAVED_POSITIVE = "positive"
        private const val SAVED_NEGATIVE = "negative"
    }
}
