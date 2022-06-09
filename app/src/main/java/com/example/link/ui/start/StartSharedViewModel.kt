package com.example.link.ui.start

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import com.example.link.ui.base.BaseViewModel
import com.example.link.util.lifecycle.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class StartSharedViewModel @Inject constructor(
    private val app: Application,
    override val savedStateHandle: SavedStateHandle
) : BaseViewModel(app){


    val navChangeEvent = SingleLiveEvent<Unit>()


    fun onNavChange(){
        navChangeEvent.call()
    }

}