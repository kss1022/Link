package com.example.link.ui.main

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import com.example.link.ui.base.BaseViewModel

class MainViewModel(
    app : Application,
    override val savedStateHandle: SavedStateHandle,
) : BaseViewModel(app){
}