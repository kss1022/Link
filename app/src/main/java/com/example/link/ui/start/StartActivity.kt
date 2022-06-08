package com.example.link.ui.start

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.core.view.isGone
import com.example.link.databinding.ActivityStartBinding
import com.example.link.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class StartActivity : BaseActivity<ActivityStartBinding, StartViewModel>() {


    override val viewModel: StartViewModel by viewModels()
    private val sharedViewModel : StartSharedViewModel by viewModels()

    @Inject
    lateinit var backPressCallback: OnBackPressedCallback


    override fun getViewBinding(): ActivityStartBinding =
        ActivityStartBinding.inflate(layoutInflater)

    override fun initViews() {
        binding.toolbar.isGone = true
    }

    override fun observeData() {
        sharedViewModel.navChangeEvent.observe(this){
            binding.toolbar.visibility = View.VISIBLE
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onBackPressedDispatcher.addCallback(this, backPressCallback)
    }


    override fun onDestroy() {
        super.onDestroy()
        backPressCallback.remove()
    }
}