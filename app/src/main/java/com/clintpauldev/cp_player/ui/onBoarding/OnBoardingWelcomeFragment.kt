package com.clintpauldev.cp_player.ui.onBoarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.clintpauldev.cp_player.databinding.OnboardingWelcomeBinding

class OnBoardingWelcomeFragment : OnBoardingFragment() {

    private var _binding: OnboardingWelcomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = OnboardingWelcomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonStart.setOnClickListener {
            onBoardingFragmentListener.onNext()
        }
    }

    companion object {
        fun newInstance(): OnBoardingFragment {
            return OnBoardingFragment()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}