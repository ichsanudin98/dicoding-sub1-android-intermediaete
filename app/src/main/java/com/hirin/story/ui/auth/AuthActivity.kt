package com.hirin.story.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.hirin.story.R
import com.hirin.story.data.localization.Localization
import com.hirin.story.databinding.ActivityAuthBinding
import com.hirin.story.ui.base.BaseActivity
import com.hirin.story.ui.localization.LocalizationBottomSheet
import com.hirin.story.utils.LocaleHelper
import com.hirin.story.utils.SharedPreferencesUtil
import com.hirin.story.utils.constant.type.SharedPrefsKeyEnum
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class AuthActivity : BaseActivity<ActivityAuthBinding>(ActivityAuthBinding::inflate), LocalizationBottomSheet.LocalizationListener {
    // <editor-fold defaultstate="collapsed" desc="initialize ui">
    private lateinit var navController: NavController
    private var localizationBottomSheet: LocalizationBottomSheet? = null
    // </editor-fold>
    private val viewModel: AuthViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (supportFragmentManager.findFragmentById(R.id.contentFragment))?.findNavController()?.let {
            navController = it
        }
    }

    @Deprecated("Deprecated in Java", ReplaceWith("onSupportNavigateUp()"))
    override fun onBackPressed() {
        onSupportNavigateUp()
    }

    override fun onSupportNavigateUp(): Boolean {
        if (!navController.navigateUp()) { // When in start destination
            binding.toastWidget.alpha = 0f
            if (getBackStackCount() == 0) {
                closeAppWithBounce()
            } else {
                onBackPressedDispatcher.onBackPressed()
            }
        }
        return navController.navigateUp()
    }

    private fun closeAppWithBounce() {
        viewModel.checkDoubleBackFlag(
            onTrue = ::finish,
            onFalse = ::showAppExitToast
        )
    }

    private fun showAppExitToast(delay:Long) = showToast(resources.getString(R.string.title_exit_app_toast), R.drawable.bg_snackbar_neutral, delay)

    fun showToast(message: String, background: Int = R.drawable.bg_snackbar_success, delay:Long = 5000) {
        binding.toastWidget.visibility = View.VISIBLE
        binding.toastWidget.background = ContextCompat.getDrawable(applicationContext, background)
        binding.toastWidget.animate().alpha(1.0f)
        binding.message.text = message
        hideToast(delay)
    }

    fun hideToast(delay:Long){
        lifecycleScope.launch {
            delay(delay)
            binding.toastWidget.animate().alpha(0.0f)
            binding.toastWidget.visibility = View.GONE
        }
    }

    fun getBackStackCount(): Int {
        val navHostFragment = supportFragmentManager.findFragmentById(binding.contentFragment.id)
        return navHostFragment?.childFragmentManager?.backStackEntryCount ?: 0
    }

    fun launchLocalizationPage() {
        if (localizationBottomSheet == null) {
            localizationBottomSheet = LocalizationBottomSheet(this@AuthActivity)
            localizationBottomSheet!!.apply {
                isCancelable = false
                show(supportFragmentManager, localizationBottomSheet!!::class.java.simpleName)
            }
        }
    }

    override fun localizationSelected(data: Localization?) {
        localizationBottomSheet?.dismiss()
        localizationBottomSheet = null

        data?.let {
            SharedPreferencesUtil.put(SharedPrefsKeyEnum.LANGUAGE_SELECTED.name, it.code)
            LocaleHelper.setNewLocale(applicationContext, it.code)
            finishAffinity()
            val intent = intent
            startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK))
        }
    }
}