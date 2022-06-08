package com.example.lint.ui.start

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import com.example.lint.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class StartSharedViewModel @Inject constructor(
    private val app: Application,
    override val savedStateHandle: SavedStateHandle
) : BaseViewModel(app){


}