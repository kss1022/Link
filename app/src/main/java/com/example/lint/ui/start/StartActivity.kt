package com.example.lint.ui.start

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.lint.databinding.ActivityStartBinding
import com.example.lint.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class StartActivity : BaseActivity<ActivityStartBinding, StartViewModel>() {


    override val viewModel: StartViewModel by viewModels()
    private val sharedViewModel : StartSharedViewModel by viewModels()


    override fun getViewBinding(): ActivityStartBinding =
        ActivityStartBinding.inflate(layoutInflater)

}