package com.example.link.ui.main

import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import com.example.link.databinding.ActivityMainBinding
import com.example.link.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {

    override val viewModel: MainViewModel by viewModels()

    override fun getViewBinding(): ActivityMainBinding =
        ActivityMainBinding.inflate(layoutInflater)


    companion object{
        fun newIntent(context : Context) = Intent(context, MainActivity::class.java)
    }
}