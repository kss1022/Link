package com.example.link.ui.start.login

import android.app.Application
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.link.R
import com.example.link.data.repository.UserRepository
import com.example.link.ui.base.BaseViewModel
import com.example.link.util.lifecycle.SingleLiveEvent
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginIdViewModel @Inject constructor(
    app: Application,
    override val savedStateHandle: SavedStateHandle,
    private val auth: FirebaseAuth,
    private val userRepository: UserRepository
) : BaseViewModel(app) {

    val nextClickEvent = SingleLiveEvent<Boolean>()
    val passwordSameEvent = SingleLiveEvent<Boolean>()
    val signInSuccessEvent = SingleLiveEvent<String?>()

    val id = savedStateHandle.getLiveData<String>(KET_ID, "")
    val password = savedStateHandle.getLiveData<String>(KEY_PASSWORD, "")
    val passwordCheck = savedStateHandle.getLiveData<String>(KEY_PASSWORD_CHECK, "")

    companion object {
        const val KET_ID = "id"
        const val KEY_PASSWORD = "password"
        const val KEY_PASSWORD_CHECK = "password_check"
    }


    fun clickNext() = viewModelScope.launch{
        val id = id.value!!

        if(checkPassWord().not()){
            // 비밀번호가 일치하지 않음
            signInSuccessEvent.value = null
        } else if(id == "" || id.length < 3){
            // id 가 너무 작음
            signInSuccessEvent.value = null
        }else{
            //로그인
            val password = password.value!!
            loginWithEmailAndPassword(id , password)
//            signInWithEmailAndPassword(id , password)
        }
    }

    /**
     * Set Value And Check Password
     */

    fun setId(str: String) {
        savedStateHandle.set(KET_ID, str)
    }

    fun setPassword(str: String) {
        savedStateHandle.set(KEY_PASSWORD, str)
        passwordSameEvent.value = checkPassWord()
    }

    fun setCheckPassword(str: String) {
        savedStateHandle.set(KEY_PASSWORD_CHECK, str)
        passwordSameEvent.value =checkPassWord()
    }


    private fun checkPassWord() : Boolean{
        val pass = password.value!!
        val check = passwordCheck.value!!

        return if (pass.length > 2 && check.length > 2) {
              pass == check
        } else {
            false
        }
    }


    /**
     * SignIn And Save User Id
     */

    private fun signInWithEmailAndPassword(email: String, password: String) = viewModelScope.launch{
        try {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        signInSuccessEvent.value = auth.currentUser?.uid!!
                    }
                    else{
                        signInSuccessEvent.value = null
                    }
                }
        }catch (e : Exception){
            signInSuccessEvent.value = null
            e.printStackTrace()
        }
    }

    fun saveUserId() = viewModelScope.launch{
        //SaveUser uid with email
        nextClickEvent.value = userRepository.saveUserId(signInSuccessEvent.value!!, id.value!!)
    }


    private fun loginWithEmailAndPassword(email: String, password: String) = viewModelScope.launch{
        try {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        signInSuccessEvent.value = auth.currentUser?.uid!!
                    } else {
                        signInSuccessEvent.value = null
                    }
                }
        }catch (e : Exception){
            signInSuccessEvent.value = null
            e.printStackTrace()
        }
    }
}