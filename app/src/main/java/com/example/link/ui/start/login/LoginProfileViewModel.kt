package com.example.link.ui.start.login

import android.app.Application
import androidx.annotation.DrawableRes
import androidx.lifecycle.SavedStateHandle
import com.example.link.R
import com.example.link.ui.base.BaseViewModel
import com.example.link.util.lifecycle.SingleLiveEvent

class LoginProfileViewModel(
    app: Application, override val savedStateHandle: SavedStateHandle
) : BaseViewModel(app) {

    val nextClickEvent = SingleLiveEvent<Boolean>(100)

    val userName = savedStateHandle.getLiveData<String>(KEY_USER_NAME, app.getString(R.string.default_user_name))
    val profile = savedStateHandle.getLiveData<Int>(KEY_PROFILE, R.drawable.profile11)

    val changeUseName = SingleLiveEvent<Unit>(100)

    companion object{
        const val KEY_USER_NAME ="user_name"
        const val KEY_PROFILE ="profile"
    }

    fun setUserName(str: String) {
        savedStateHandle.set(KEY_USER_NAME, str)
    }

    fun setProfile(@DrawableRes id: Int) {
        savedStateHandle.set(KEY_PROFILE, id)
    }

    fun changeUserName() {
        changeUseName.call()
    }

    fun clickNext() {
        nextClickEvent.value = true
    }
}