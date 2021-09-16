package com.app.xarehub.ui.addCard

import android.Manifest
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.app.xarehub.R
import com.app.xarehub.base.ProgressIndicatorFragment
import com.app.xarehub.databinding.FragmentAddNewCardBinding
import com.app.xarehub.ext.setupToolbar
import com.app.xarehub.ext.toast
import dagger.hilt.android.AndroidEntryPoint
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import tech.cherri.tpdirect.api.TPDCard
import tech.cherri.tpdirect.callback.TPDCardGetPrimeSuccessCallback
import tech.cherri.tpdirect.callback.TPDGetPrimeFailureCallback
import tech.cherri.tpdirect.model.TPDStatus

@AndroidEntryPoint
class AddCardFragment : ProgressIndicatorFragment(), EasyPermissions.PermissionCallbacks {

    companion object {
        private const val RC_CALL_PHONE = 64
    }

    private var initTpdFormDone = false
    private var tpdCard: TPDCard? = null

    private val viewModel: AddCardViewModel by viewModels()

    private var _binding: FragmentAddNewCardBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        _binding = FragmentAddNewCardBinding.inflate(inflater, container, false)

        setupToolbar(
            binding.toolbarAddNewCard,
            "輸入卡號",
            backValid = true,
            closeValid = false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        binding.btnAddCard.setOnClickListener {
            tpdCard?.getPrime()
        }
    }

    override fun onResume() {
        super.onResume()

        requiresPhoneStatePermission()
    }

    private fun initTpdForm() {

        if (initTpdFormDone) return

        // 1.Setup environment.
//        val serverType = if (BuildConfig.DEBUG) TPDServerType.Sandbox else TPDServerType.Production
//        TPDSetup.initInstance(
//            requireActivity(),
//            BuildConfig.TAPPAY_APP_ID.toInt(),
//            BuildConfig.TAPPAY_APP_KEY,
//            serverType)

        //2.Setup input form
        binding.apply {
            tpdForm.setTextErrorColor(Color.RED)
            tpdForm.setOnFormUpdateListener { tpdStatus ->
                tpdTip.visibility = View.GONE
                tpdTip.text = when {
                    tpdStatus.cardNumberStatus == TPDStatus.STATUS_ERROR -> {
                        tpdTip.visibility = View.VISIBLE
                        "Invalid Card Number"
                    }
                    tpdStatus.expirationDateStatus == TPDStatus.STATUS_ERROR -> {
                        tpdTip.visibility = View.VISIBLE
                        "Invalid Expiration Date"
                    }
                    tpdStatus.ccvStatus == TPDStatus.STATUS_ERROR -> {
                        tpdTip.visibility = View.VISIBLE
                        "Invalid CCV"
                    }
                    else -> ""
                }
                btnAddCard.isEnabled = tpdStatus.isCanGetPrime
            }

            //3.Setup TPDCard with form and callbacks.
            val tpdCardGetPrimeSuccessCallback =
                TPDCardGetPrimeSuccessCallback { prime, cardInfo, cardIdentifier, merchantReferenceInfo ->
                    Log.d("Tap Pay Test", "prime:  $prime")
                    Log.d("Tap Pay Test", "cardInfo:  $cardInfo")
                    Log.d("Tap Pay Test", "cardIdentifier:  $cardIdentifier")
                    Log.d("Tap Pay Test", "merchantReferenceInfo:  $merchantReferenceInfo")
                    toast("Get Prime Success")
                }
            val tpdGetPrimeFailureCallback =
                TPDGetPrimeFailureCallback { status, reportMsg ->
                    Log.d("Tap Pay Test", "failure: $status: $reportMsg")
                    toast("Get Prime Failure")
                }

            tpdCard = TPDCard.setup(tpdForm)
                .onSuccessCallback(tpdCardGetPrimeSuccessCallback)
                .onFailureCallback(tpdGetPrimeFailureCallback)

            tpdForm.visibility = View.VISIBLE
        }

        initTpdFormDone = true
    }

    private fun requiresPhoneStatePermission() {
        val perms = arrayOf(
            Manifest.permission.READ_PHONE_STATE,
        )
        if (EasyPermissions.hasPermissions(requireContext(), *perms)) {
            initTpdForm()
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(
                this, "需要電話",
                RC_CALL_PHONE, *perms
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            // Do something after user returned from app settings screen, like showing a Toast.

            val perms = arrayOf(
                Manifest.permission.READ_PHONE_STATE,
            )

            if (EasyPermissions.hasPermissions(requireContext(), *perms)) {
                toast("取得電話權限")
            } else {
                toast("無法取得電話權限")
            }
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        initTpdForm()
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {

        // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
        // This will display a dialog directing them to enable the permission in app settings.
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}