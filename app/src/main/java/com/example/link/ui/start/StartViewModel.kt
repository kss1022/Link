package com.example.link.ui.start

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import com.example.link.ui.base.BaseViewModel

class StartViewModel(
    app : Application,
    override val savedStateHandle: SavedStateHandle,
) : BaseViewModel(app){
}