package com.example.yourchoice.data.preference

import android.content.SharedPreferences

class PreferenceManager(private val preferences: SharedPreferences) {

    companion object {
        const val PREFERENCES_NAME = "com.example.lint"

        private const val INVALID_STRING_VALUE = ""
        private const val INVALID_BOOLEAN_VALUE = false
        private const val INVALID_LONG_VALUE = Long.MIN_VALUE
        private const val INVALID_INT_VALUE = Int.MIN_VALUE

        private const val USER_LONG_KEY = "user_long"
        private const val BASE_STRING_KEY = "base_string"
    }

    private val editor by lazy { preferences.edit() }


    fun clear() {
        editor.clear()
        editor.apply()
    }


    /**
     * String 값 저장
     */
    fun setString(value: String) {
        editor.putString(BASE_STRING_KEY, value)
        editor.apply()
    }

    fun getString(): String? {
        val value = preferences.getString(BASE_STRING_KEY, INVALID_STRING_VALUE)

        return if (value == "") {
            null
        } else {
            value
        }
    }
}