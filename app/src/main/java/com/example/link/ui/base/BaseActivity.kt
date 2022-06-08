package com.example.link.ui.base


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding



abstract class BaseActivity<VB : ViewBinding, VM : BaseViewModel> : AppCompatActivity() {

    abstract val viewModel: VM
    protected lateinit var binding: VB

    abstract fun getViewBinding(): VB


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getViewBinding()
        setContentView(binding.root)

        initViews()
        bindViews()
        observeData()
    }



    open fun initViews() = Unit

    open fun bindViews() = Unit

    open fun observeData() = Unit
}