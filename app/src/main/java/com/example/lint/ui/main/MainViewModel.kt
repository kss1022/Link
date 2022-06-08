package com.example.lint.ui.main

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import com.example.lint.ui.base.BaseViewModel

class MainViewModel(
    app : Application,
    override val savedStateHandle: SavedStateHandle,
) : BaseViewModel(app){
}