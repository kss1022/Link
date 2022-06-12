package com.example.link.ui.main.detail.fragmentlist

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import com.example.link.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailSleepViewModel  @Inject constructor(
    app : Application,
    override val savedStateHandle: SavedStateHandle
) : BaseViewModel(app) {

    val menu: LiveData<String> = savedStateHandle.getLiveData(KEY_SLEEP_TOP_MENU, TODAY)

    companion object {
        const val TODAY = "day"
        const val RECORD = "record"
        const val ANALYSIS = "analysis"

        const val KEY_SLEEP_TOP_MENU = "sleep_top_menu"
    }


    fun setTopMenu(menu: String) {
        savedStateHandle.set(KEY_SLEEP_TOP_MENU, menu)
    }
}