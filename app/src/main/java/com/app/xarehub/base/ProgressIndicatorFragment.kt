package com.app.xarehub.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.kaopiz.kprogresshud.KProgressHUD
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by PS Wang on 2021/8/4
 */
@AndroidEntryPoint
open class ProgressIndicatorFragment : Fragment() {

    private lateinit var progressIndicator: KProgressHUD

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        progressIndicator = KProgressHUD.create(requireActivity())
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setCancellable(false)
            .setAnimationSpeed(2)
            .setDimAmount(0.5f)
    }

    protected fun showProgressIndicator() {
        if (!progressIndicator.isShowing) {
            progressIndicator.show()
        }
    }

    protected fun hideProgressIndicator() {
        if (progressIndicator.isShowing) {
            progressIndicator.dismiss()
        }
    }
}