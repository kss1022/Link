package com.example.link.ui.main.detail.fragmentlist

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.example.link.model.EatMemoModel
import com.example.link.ui.base.BaseViewModel
import com.example.link.util.lifecycle.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailWalkViewModel @Inject constructor(
    app: Application,
    override val savedStateHandle: SavedStateHandle
) : BaseViewModel(app) {

    val menu: LiveData<String> = savedStateHandle.getLiveData(KEY_WALK_TOP_MENU, STEP)

    val isWalk = SingleLiveEvent<Boolean>(300)
    val isBottomViewVisible = SingleLiveEvent<Boolean>(300)

    companion object {
        const val STEP = "step"
        const val GPS = "gps"
        const val ANALYSIS = "analysis"

        const val KEY_WALK_TOP_MENU = "walk_top_menu"
    }


    fun setTopMenu(menu: String) {
        savedStateHandle.set(KEY_WALK_TOP_MENU, menu)
    }

    fun clickWalkButton() {
        isWalk.value?.let {
            isWalk.value = it.not()
        } ?: kotlin.run {
            isWalk.value = true
        }

    }

    fun clickBottomButton() {
        isBottomViewVisible.value?.let {
            isBottomViewVisible.value = it.not()
        } ?: kotlin.run {
            isBottomViewVisible.value = true
        }
    }
}