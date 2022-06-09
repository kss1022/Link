package com.example.yourchoice.data.preference

import android.content.SharedPreferences

class PreferenceManager(private val preferences: SharedPreferences) {

    companion object {
        const val PREFERENCES_NAME = "com.example.lint"

        private const val INVALID_STRING_VALUE = ""
        private const val INVALID_BOOLEAN_VALUE = false
        private const val INVALID_LONG_VALUE = Long.MIN_VALUE
        private const val INVALID_INT_VALUE = Int.MIN_VALUE

        private const val USER_ID_STRING_KEY = "user_id"
        private const val USER_PROFILE_INT_KEY = "profile_id"
    }

    private val editor by lazy { preferences.edit() }


    fun clear() {
        editor.clear()
        editor.apply()
    }


    /**
     * Save User Id ( String 값 저장)
     */
    fun setUserId(value: String) {
        editor.putString(USER_ID_STRING_KEY, value)
        editor.apply()
    }

    fun getUserId(): String? {
        val value = preferences.getString(USER_ID_STRING_KEY, INVALID_STRING_VALUE)

        return if (value == INVALID_STRING_VALUE) {
            null
        } else {
            value
        }
    }


    /**
     * Save Profile Id ( Int 값 저장)
     */
    fun setProfileId(value: Int) {
        editor.putInt(USER_PROFILE_INT_KEY, value)
        editor.apply()
    }

    fun getProfileId(): Int? {
        val value = preferences.getInt(USER_PROFILE_INT_KEY, INVALID_INT_VALUE)

        return if (value == INVALID_INT_VALUE) {
            null
        } else {
            value
        }
    }
}