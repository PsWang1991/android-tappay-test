package com.app.xarehub.ext

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.app.xarehub.MainActivity
import com.app.xarehub.R
import com.app.xarehub.databinding.LayoutToolbarBinding

/**
 * Created by PS Wang on 2021/7/21
 */

fun Context.showMessage(title: String, message: String) {
    AlertDialog.Builder(this).apply {
        setTitle(title)
        setMessage(message)
        setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
        create()
        show()
    }
}

fun Fragment.showMessage(title: String, message: String) {
    requireContext().showMessage(title, message)
}

fun Fragment.showErrorMessage(message: String) {
    requireContext().showMessage("Error", message)
}

fun Context.toast(content: String) {
    Toast.makeText(this, content, Toast.LENGTH_SHORT).show()
}

fun Fragment.toast(content: String) {
    requireContext().toast(content)
}

fun Fragment.setBottomNavItem(item: MainActivity.BottomNavItem) {
    (requireActivity() as MainActivity).setBottomNavSelected(item)
}

fun Fragment.setupToolbar(
    toolbar: LayoutToolbarBinding,
    title: String,
    backValid: Boolean, closeValid: Boolean
) {

    toolbar.apply {

        toolbarTitle.text = title

        if (backValid) {
            btnBack.visibility = View.VISIBLE
            btnBack.setOnClickListener {
                requireActivity().onBackPressed()
            }
        }

        if (closeValid) {
            btnClose.visibility = View.VISIBLE
            btnClose.setOnClickListener {
                requireActivity().onBackPressed()
            }
        }
    }
}