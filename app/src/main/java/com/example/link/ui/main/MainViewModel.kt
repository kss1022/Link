package com.example.link.ui.main

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.link.data.repository.PetRepository
import com.example.link.ui.base.BaseViewModel
import com.example.yourchoice.data.preference.PreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    app : Application,
    override val savedStateHandle: SavedStateHandle,
    private val preferenceManager: PreferenceManager,
    private val petRepository: PetRepository
) : BaseViewModel(app){


    val profileImage = MutableLiveData<Int>(preferenceManager.getProfileId())

    private val nfcTag = MutableLiveData<String>()
    val nfcMessage = MutableLiveData<String>()

    fun setTag( tag : String?) = viewModelScope.launch{
        tag?.let {
            nfcTag.value = it
        }
    }

    fun setNfcMessage(result: String) = viewModelScope.launch {
        val isExist = petRepository.checkPetIsExist(result)

        if(isExist){
            nfcMessage.value = result
        }else{
            //error
        }
    }


}