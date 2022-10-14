package com.hirin.story.ui.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.hirin.story.utils.LocaleHelper

abstract class BaseActivity<B : ViewBinding>(val bindingFactory: (LayoutInflater) -> B) : AppCompatActivity() {
    // <editor-fold defaultstate="collapsed" desc="initialize ui">
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="initialize text">
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="initialize data">
    var goTo: Intent? = null
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="initialize observable">
    // </editor-fold>

    lateinit var binding: B

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase?.let { LocaleHelper.setLocale(it) } ?: run { newBase })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = bindingFactory(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        goTo = null
    }
}