package com.example.link.ui.start.login

import android.app.Application
import android.util.Log
import android.view.View
import androidx.core.view.isGone
import androidx.lifecycle.SavedStateHandle
import com.example.link.ui.base.BaseViewModel
import com.example.link.util.lifecycle.SingleLiveEvent

class LoginIdViewModel(
    app: Application, override val savedStateHandle: SavedStateHandle
) : BaseViewModel(app) {

    val nextClickEvent = SingleLiveEvent<Boolean>(100)
    val passwordSameEvent = SingleLiveEvent<Boolean>()

    val id = savedStateHandle.getLiveData<String>(KET_ID, "")
    val password = savedStateHandle.getLiveData<String>(KEY_PASSWORD, "")
    val passwordCheck = savedStateHandle.getLiveData<String>(KEY_PASSWORD_CHECK, "")

    companion object {
        const val KET_ID = "id"
        const val KEY_PASSWORD = "password"
        const val KEY_PASSWORD_CHECK = "password_check"
    }


    fun clickNext() {
        //todo Google 로그인하기
        nextClickEvent.value = passwordSameEvent.value
    }

    fun setId(str: String) {
        savedStateHandle.set(KET_ID, str)
    }

    fun setPassword(str: String) {
        savedStateHandle.set(KEY_PASSWORD, str)
        checkPassWord()
    }

    fun setCheckPassword(str: String) {
        savedStateHandle.set(KEY_PASSWORD_CHECK, str)
        checkPassWord()
    }


    private fun checkPassWord() {
        val pass = password.value!!
        val check = passwordCheck.value!!

        if (pass.length > 2 && check.length > 2) {
            passwordSameEvent.value = pass == check
        } else {
            passwordSameEvent.value = false
        }
    }

}