package com.example.link.ui.main.my

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.example.link.ui.base.BaseViewModel
import com.example.yourchoice.data.preference.PreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyViewModel @Inject constructor(
    app: Application,
    override val savedStateHandle: SavedStateHandle,
    private val preferenceManager: PreferenceManager
) : BaseViewModel(app) {

    val profileImage = MutableLiveData<Int>(preferenceManager.getProfileId())
}