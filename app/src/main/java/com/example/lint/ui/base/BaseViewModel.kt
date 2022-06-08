package com.example.lint.ui.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle


abstract class BaseViewModel(
    private val app: Application
) : AndroidViewModel(app){
    abstract val savedStateHandle: SavedStateHandle
}