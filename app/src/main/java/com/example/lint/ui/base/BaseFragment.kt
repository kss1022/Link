package com.example.lint.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding


abstract class BaseFragment<VB : ViewBinding, VM : BaseViewModel> : Fragment() {

    abstract val viewModel: VM
    private var _binding  : VB? = null
    protected val binding get() = _binding!!


    abstract fun getViewBinding() : VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = getViewBinding()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        bindViews()
        observeData()
    }



    open fun initViews() = Unit

    open fun bindViews() = Unit

    open fun observeData() = Unit


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}