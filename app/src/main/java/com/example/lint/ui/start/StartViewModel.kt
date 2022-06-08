package com.example.lint.ui.start

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import com.example.lint.ui.base.BaseViewModel

class StartViewModel(
    app : Application,
    override val savedStateHandle: SavedStateHandle,
) : BaseViewModel(app){
}