package com.example.link.ui.main

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.link.data.repository.PetRepository
import com.example.link.data.repository.UserRepository
import com.example.link.model.PetModel
import com.example.link.model.User
import com.example.link.ui.base.BaseViewModel
import com.example.link.util.lifecycle.SingleLiveEvent
import com.example.yourchoice.data.preference.PreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainSharedViewModel @Inject constructor(
    app: Application,
    override val savedStateHandle: SavedStateHandle,
    private val userRepository: UserRepository,
    private val petRepository: PetRepository,
    private val preferenceManager: PreferenceManager
) : BaseViewModel(app){

    val userId : LiveData<String> = savedStateHandle.getLiveData(KEY_USER_ID, "")

    val petModel = MutableLiveData<PetModel>()
    val userModel = MutableLiveData<User>()

    companion object{
        const val KEY_USER_ID = "user_id"
    }

    fun fetchData() = viewModelScope.launch{
        val id = preferenceManager.getUserId()
        savedStateHandle.set(KEY_USER_ID, id)

        id?.let { _->
            val user = userRepository.getUserData(id)
            val pet = petRepository.getPetData(id)
            pet?.let {
                petModel.value = it
            } ?:{
                //get Pet Data error
            }

            user?.let {
                userModel.value = it
            } ?:{
                //get User Data error
            }

        } ?: kotlin.run {
            //user id error
        }
    }

}