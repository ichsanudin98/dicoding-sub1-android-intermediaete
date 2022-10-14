package com.hirin.story.utils

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import androidx.annotation.StringDef
import com.hirin.story.utils.constant.type.SharedPrefsKeyEnum
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.util.*

class LocaleHelper {
    companion object {
        const val ENGLISH = "en"
        const val INDONESIA = "in"
        val SPANISH = "es"

        @Retention(RetentionPolicy.SOURCE)
        @StringDef(ENGLISH, INDONESIA)
        annotation class LocaleDef {
            companion object {
                var SUPPORTED_LOCALES = arrayOf(ENGLISH, INDONESIA)
            }
        }

        /**
         * set current pref locale
         */
        fun setLocale(mContext: Context): Context? {
            return try {
                updateResources(
                    mContext,
                    SharedPreferencesUtil[SharedPrefsKeyEnum.LANGUAGE_SELECTED.name, String::class.java,
                            getLocale(mContext.resources).language]
                )
            } catch (e: Exception) {
                updateResources(
                    mContext,
                    getLocale(mContext.resources).getLanguage()
                )
            }
        }

        /**
         * Set new Locale with context
         */
        fun setNewLocale(mContext: Context, @LocaleDef language: String?): Context? {
            SharedPreferencesUtil.put(SharedPrefsKeyEnum.LANGUAGE_SELECTED.name, language)
            return updateResources(mContext, language)
        }

        /**
         * update resource
         */
        private fun updateResources(context: Context, language: String?): Context? {
            var context = context
            val locale = Locale(language)
            Locale.setDefault(locale)
            val res = context.resources
            val config = Configuration(res.configuration)
            config.setLocale(locale)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                context = context.createConfigurationContext(config)
            } else {
                res.updateConfiguration(config, res.displayMetrics)
            }
            return context
        }

        /**
         * get current locale
         */
        fun getLocale(res: Resources): Locale {
            val config = res.configuration
            return if (Build.VERSION.SDK_INT >= 24) config.locales[0] else config.locale
        }
    }
}