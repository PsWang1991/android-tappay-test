package com.app.xarehub.ui.key

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.app.xarehub.base.ProgressIndicatorFragment
import com.app.xarehub.databinding.FragmentKeyBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class KeyFragment : ProgressIndicatorFragment() {

    private val viewModel: KeyViewModel by viewModels()
    private var _binding: FragmentKeyBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        _binding = FragmentKeyBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}