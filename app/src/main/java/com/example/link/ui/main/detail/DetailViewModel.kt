package com.example.link.ui.main.detail

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import com.example.link.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    app: Application,
    override val savedStateHandle: SavedStateHandle,
) : BaseViewModel(app) {


}