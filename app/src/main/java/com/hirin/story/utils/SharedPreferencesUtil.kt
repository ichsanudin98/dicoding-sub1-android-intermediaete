package com.hirin.story.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.hirin.story.utils.constant.AppConstant

object SharedPreferencesUtil {
    private lateinit var shared: SharedPreferences
    private lateinit var edit: SharedPreferences.Editor

    fun init(c: Context) {
        init(c, AppConstant.SHAREDPREFS_NAME, Context.MODE_PRIVATE)
    }

    fun init(context: Context, preferenceName: String?, mode: Int) {
        shared = context.getSharedPreferences(
            preferenceName,
            mode
        )
        edit = shared.edit()
        edit.apply()
    }

    //==================================================================//
    //==================================================================//
    /**
     * Save data in shared preferences
     *
     * @param key is key for data
     * @param value is value for data
     */
    fun <T> put(key: String?, value: T) {
        if (edit == null) {
            Log.e(this.javaClass.simpleName, "You must call begin() first.")
            return
        }
        when (value) {
            is String -> {
                edit.putString(key, value as String)
                edit.apply()
            }
            is Int -> {
                edit.putInt(key, (value as Int))
                edit.apply()
            }
            is Float -> {
                edit.putFloat(key, (value as Float))
                edit.apply()
            }
            is Long -> {
                edit.putLong(key, (value as Long))
                edit.apply()
            }
            is Boolean -> {
                edit.putBoolean(key, (value as Boolean))
                edit.apply()
            }
            else -> {
                Log.e(this.javaClass.simpleName, "Unsupported value type.")
            }
        }
    }

    /**
     * Get data by key in shared preferences
     *
     * @param key is key for data set
     * @param mClass is class from data set
     * @param mDefaultValue is default value from data set if data is null
     */
    operator fun <T> get(key: String?, mClass: Class<T>?, mDefaultValue: T?): T? {
        return when {
            String::class.java == mClass -> {
                shared.getString(
                    key,
                    if (mDefaultValue != null) mDefaultValue as String? else null
                ) as T
            }
            Int::class.java == mClass -> {
                Integer.valueOf(
                    shared.getInt(
                        key,
                        (if (mDefaultValue != null) mDefaultValue as Int? else -9999)!!
                    )
                ) as T
            }
            Float::class.java == mClass -> {
                java.lang.Float.valueOf(
                    shared.getFloat(
                        key,
                        (if (mDefaultValue != null) mDefaultValue as Float? else -9999f)!!
                    )
                ) as T
            }
            Long::class.java == mClass -> {
                java.lang.Long.valueOf(
                    shared.getLong(
                        key,
                        (if (mDefaultValue != null) mDefaultValue as Long? else -9999L)!!
                    )
                ) as T
            }
            Boolean::class.java == mClass -> {
                java.lang.Boolean.valueOf(
                    shared.getBoolean(
                        key,
                        mDefaultValue != null && mDefaultValue as Boolean
                    )
                ) as T
            }
            else -> {
                null
            }
        }
    }

    /**
     * Get data by key in shared preferences
     *
     * @param key is key for data set
     * @param mClass is class from data set
     */
    operator fun <T> get(key: String?, mClass: Class<T>?): T? {
        return get(key, mClass, null)
    }


    //==================================================================//

    //==================================================================//
    /**
     * Checking data from key to have value or not in shared preference
     */
    fun isExist(key: String?): Boolean {
        return shared.contains(key)
    }

    /**
     * Remove value from sharedPreference
     */
    fun remove(key: String?) {
        edit.remove(key)
        edit.apply()
    }

    fun clearAllSharedPreferences() {
        edit.clear()
        edit.apply()
    }
}