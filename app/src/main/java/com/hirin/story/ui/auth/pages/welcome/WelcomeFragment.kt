package com.hirin.story.ui.auth.pages.welcome

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.hirin.story.R
import com.hirin.story.databinding.FragmentWelcomeBinding
import com.hirin.story.ui.auth.AuthActivity
import com.hirin.story.ui.base.BaseFragment
import com.hirin.story.ui.main.MainActivity
import com.hirin.story.utils.LocaleHelper
import com.hirin.story.utils.SharedPreferencesUtil
import com.hirin.story.utils.constant.type.SharedPrefsKeyEnum
import com.hirin.story.utils.extension.navigateSlideHorizontal

class WelcomeFragment : BaseFragment<FragmentWelcomeBinding>() {
    override fun getViewBinding(): FragmentWelcomeBinding =
        FragmentWelcomeBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        if (SharedPreferencesUtil[SharedPrefsKeyEnum.TOKEN.name, String::class.java, ""]!!.isNotEmpty()) {
            startActivity(Intent(requireActivity(), MainActivity::class.java))
            requireActivity().finishAffinity()
        }

        with(binding) {
            btLogin.setOnClickListener {
                findNavController().navigateSlideHorizontal(WelcomeFragmentDirections.actionToLoginFragment())
            }
            btRegister.setOnClickListener {
                findNavController().navigateSlideHorizontal(WelcomeFragmentDirections.actionToRegisterFragment())
            }
            cvLanguage.setOnClickListener {
                (requireActivity() as AuthActivity).launchLocalizationPage()
            }

            SharedPreferencesUtil[SharedPrefsKeyEnum.LANGUAGE_SELECTED.name, String::class.java, LocaleHelper.INDONESIA]!!.also {
                when(it) {
                    LocaleHelper.INDONESIA -> {
                        igLanguage.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ig_flag_indonesian))
                    }
                    LocaleHelper.ENGLISH -> {
                        igLanguage.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ig_flag_usa))
                    }
                }
            }

            val head = ObjectAnimator.ofFloat(tbHead, View.ALPHA, 1f).setDuration(500)
            val body = ObjectAnimator.ofFloat(tbBody, View.ALPHA, 1f).setDuration(500)
            val login = ObjectAnimator.ofFloat(btLogin, View.ALPHA, 1f).setDuration(500)
            val register = ObjectAnimator.ofFloat(btRegister, View.ALPHA, 1f).setDuration(500)

            AnimatorSet().apply {
                playSequentially(head, body, login, register)
                start()
            }
        }
    }
}