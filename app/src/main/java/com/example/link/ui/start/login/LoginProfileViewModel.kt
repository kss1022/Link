package com.example.link.ui.start.login

import android.app.Application
import androidx.annotation.DrawableRes
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.link.R
import com.example.link.data.repository.UserRepository
import com.example.link.ui.base.BaseViewModel
import com.example.link.util.lifecycle.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginProfileViewModel @Inject constructor(
    app: Application,
    override val savedStateHandle: SavedStateHandle,
    private val userRepository: UserRepository
) : BaseViewModel(app) {

    val nextClickEvent = SingleLiveEvent<Boolean>(100)

    val userName = savedStateHandle.getLiveData<String>(KEY_USER_NAME, app.getString(R.string.default_user_name))
    val profile = savedStateHandle.getLiveData<Int>(KEY_PROFILE, R.drawable.profile11)
    private val profilePos = savedStateHandle.getLiveData<Int>(KEY_PROFILE_POSITION, 11-1)

    val changeUseName = SingleLiveEvent<Unit>(100)

    companion object{
        const val KEY_USER_NAME ="user_name"
        const val KEY_PROFILE ="profile"
        const val KEY_PROFILE_POSITION ="profile_pos"
    }

    fun setUserName(str: String) {
        savedStateHandle.set(KEY_USER_NAME, str)
    }

    fun setProfile(@DrawableRes id: Int, position: Int) {
        savedStateHandle.set(KEY_PROFILE, id)
        savedStateHandle.set(KEY_PROFILE_POSITION, position)
    }

    fun changeUserName() {
        changeUseName.call()
    }

    fun clickNext() = viewModelScope.launch{
        userRepository.saveUserNameWithProfileNum(userName.value!!, profile.value!!,profilePos.value!!)
        nextClickEvent.value = true
    }
}