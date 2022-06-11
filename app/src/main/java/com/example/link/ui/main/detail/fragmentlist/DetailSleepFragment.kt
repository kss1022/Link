package com.example.link.ui.main.detail.fragmentlist

import androidx.fragment.app.viewModels
import com.example.link.databinding.FragmentDetailAllBinding
import com.example.link.databinding.FragmentDetailSleepBinding
import com.example.link.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailSleepFragment : BaseFragment<FragmentDetailSleepBinding, DetailSleepViewModel>(){

    override val viewModel: DetailSleepViewModel by viewModels()

    override fun getViewBinding(): FragmentDetailSleepBinding =
        FragmentDetailSleepBinding.inflate(layoutInflater)

    override fun initViews() {

    }

    override fun bindViews() {

    }

    override fun observeData() {

    }

    companion object{
        fun newInstance() = DetailSleepFragment()
    }
}