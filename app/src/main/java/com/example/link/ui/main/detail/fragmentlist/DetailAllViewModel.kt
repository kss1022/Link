package com.example.link.ui.main.detail.fragmentlist

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.example.link.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailAllViewModel @Inject constructor(
    app : Application,
    override val savedStateHandle: SavedStateHandle
) :BaseViewModel(app) {

    val menu : LiveData<String> = savedStateHandle.getLiveData(KEY_TOP_MENU, "day")

    companion object{
        const val DAY = "day"
        const val  WEEK = "week"
        const val  MONTH = "month"
        const val  YEAR = "year"

        const val KEY_TOP_MENU = "top_menu"
    }

    fun setTopMenu( menu : String){
        savedStateHandle.set(KEY_TOP_MENU, menu)
    }

}