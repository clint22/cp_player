package com.clintpauldev.cp_player.ui.onBoarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.clintpauldev.cp_player.databinding.OnboardingPermissionBinding
import com.clintpauldev.cp_player.tools.INITIAL_PERMISSION_ASKED
import com.clintpauldev.cp_player.tools.Settings

class OnBoardingPermissionFragment : OnBoardingFragment() {

    private var _binding: OnboardingPermissionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = OnboardingPermissionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonGrantPermission.setOnClickListener {
            onBoardingFragmentListener.onPermission()
        }

        binding.buttonNext.visibility = if (Settings.getInstance(requireActivity()).getBoolean(
                INITIAL_PERMISSION_ASKED, false
            )
        ) View.VISIBLE else View.GONE

        binding.buttonNext.setOnClickListener {
            onBoardingFragmentListener.onPermission()
        }

    }

    companion object {
        fun newInstance(): OnBoardingPermissionFragment {
            return OnBoardingPermissionFragment()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}