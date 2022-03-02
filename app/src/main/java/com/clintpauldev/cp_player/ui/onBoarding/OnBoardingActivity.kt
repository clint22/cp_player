package com.clintpauldev.cp_player.ui.onBoarding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import com.clintpauldev.cp_player.R
import com.clintpauldev.cp_player.databinding.ActivityOnBoardingBinding
import com.clintpauldev.cp_player.helpers.StoragePermissionsDelegate.Companion.getStoragePermission
import com.clintpauldev.cp_player.util.Permissions
import kotlinx.coroutines.launch

class OnBoardingActivity : AppCompatActivity(), OnBoardingFragmentListener {

    private lateinit var binding: ActivityOnBoardingBinding

    private val viewModel: OnBoardingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showFragment(viewModel.fragmentName)
    }

    private fun showFragment(fragmentName: FragmentName) {

        val fragment =
            supportFragmentManager.getFragment(Bundle(), fragmentName.name) ?: when (fragmentName) {
                FragmentName.WELCOME -> OnBoardingWelcomeFragment.newInstance()
                FragmentName.ASK_PERMISSION -> OnBoardingPermissionFragment.newInstance()
                FragmentName.SCAN -> {}
                FragmentName.NO_PERMISSION -> {}
            }
        (fragment as OnBoardingFragment).onBoardingFragmentListener = this
        supportFragmentManager.commit {
            setCustomAnimations(
                R.anim.anim_enter_right,
                R.anim.anim_leave_left,
                android.R.anim.fade_in,
                android.R.anim.fade_out
            )
            replace(R.id.fragment_onboarding_placeholder, fragment, fragmentName.name)
        }
        viewModel.fragmentName = fragmentName

    }

    override fun onNext() {
        when (viewModel.fragmentName) {
            FragmentName.WELCOME -> if (Permissions.canReadStorage(this)) showFragment(FragmentName.SCAN) else showFragment(
                FragmentName.ASK_PERMISSION
            )
            FragmentName.ASK_PERMISSION -> {}
            FragmentName.SCAN -> {}
            FragmentName.NO_PERMISSION -> {}
        }
    }

    override fun onDone() {
        TODO("Not yet implemented")
    }

    override fun onPermission() {
        lifecycleScope.launch {
            getStoragePermission()
            onNext()
        }
    }
}


enum class FragmentName {

    WELCOME,
    ASK_PERMISSION,
    SCAN,
    NO_PERMISSION
}