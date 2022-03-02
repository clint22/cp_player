package com.clintpauldev.cp_player.ui.onBoarding

import androidx.fragment.app.Fragment

open class OnBoardingFragment : Fragment() {

    lateinit var onBoardingFragmentListener: OnBoardingFragmentListener
}

interface OnBoardingFragmentListener {

    fun onNext()
    fun onDone()
    fun onPermission()

}