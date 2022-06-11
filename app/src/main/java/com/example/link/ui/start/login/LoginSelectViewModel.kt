package com.example.link.ui.start.login

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.link.data.repository.PetRepository
import com.example.link.data.repository.UserRepository
import com.example.link.ui.base.BaseViewModel
import com.example.link.util.lifecycle.SingleLiveEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginSelectViewModel @Inject constructor(
    app: Application,
    override val savedStateHandle: SavedStateHandle,
    private val userRepository: UserRepository,
    private val petRepository: PetRepository,
    private val auth: FirebaseAuth
) : BaseViewModel(app) {

    val googleLoginClickEvent = SingleLiveEvent<Unit>(300)
    val loginSuccess = SingleLiveEvent<Boolean>()
    val saveUserData = SingleLiveEvent<Boolean>()

    val id : LiveData<String> = savedStateHandle.getLiveData(KEY_ID, "")
    val email : LiveData<String> = savedStateHandle.getLiveData(KEY_EMAIL, "")
    val idToken : LiveData<String> = savedStateHandle.getLiveData(KEY_ID_TOKEN, "")

    companion object {
        const val KEY_ID = "id"
        const val KEY_EMAIL = "email"
        const val KEY_ID_TOKEN = "idToken"
    }

    fun clickGoogleLogin() {
        googleLoginClickEvent.call()
    }

    fun setUserIdAndToken(id: String?, idToken: String?){
        savedStateHandle.set(KEY_ID_TOKEN, idToken)
        googleLogin()
    }


    private fun googleLogin() = viewModelScope.launch {
        val credential = GoogleAuthProvider.getCredential(idToken.value, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    savedStateHandle.set(KEY_ID, auth.currentUser?.uid)
                    savedStateHandle.set(KEY_EMAIL, auth.currentUser?.email)
                    loginSuccess.value = true
                } else {
                    loginSuccess.value = false
                }
            }
    }

    fun saveUserData() = viewModelScope.launch {
        val userIsExist = userRepository.checkUserIsExist(id.value!!)
        val petIsExist = petRepository.checkPetIsExist(id.value!!)

        if(userIsExist && petIsExist){
            //이미 정보가 있는 회원인 경우
            userRepository.saveExistUserData(id.value!!, idToken.value!!)
            userRepository.getUserProfileNum(id.value!!)
        }else{
            //처음인 경우
            Log.e("Save User Data", "${id.value}, ${email.value}, ${idToken.value}")
            userRepository.saveUserId(id.value!!, email.value!!, idToken.value!!)
        }

        saveUserData.value = userIsExist && petIsExist
    }

}