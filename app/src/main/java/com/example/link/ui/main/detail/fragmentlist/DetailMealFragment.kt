package com.example.link.ui.main.detail.fragmentlist

import androidx.fragment.app.viewModels
import com.example.link.databinding.FragmentDetailAllBinding
import com.example.link.databinding.FragmentDetailMealBinding
import com.example.link.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailMealFragment : BaseFragment<FragmentDetailMealBinding, DetailMealViewModel>(){

    override val viewModel: DetailMealViewModel by viewModels()

    override fun getViewBinding(): FragmentDetailMealBinding =
        FragmentDetailMealBinding.inflate(layoutInflater)

    override fun initViews() {

    }

    override fun bindViews() {

    }

    override fun observeData() {

    }

    companion object{
        fun newInstance() = DetailMealFragment()
    }
}