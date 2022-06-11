package com.example.link.ui.main.detail.fragmentlist

import androidx.fragment.app.viewModels
import com.example.link.databinding.FragmentDetailAllBinding
import com.example.link.databinding.FragmentDetailWalkBinding
import com.example.link.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DetailWalkFragment : BaseFragment<FragmentDetailWalkBinding, DetailWalkViewModel>(){

    override val viewModel: DetailWalkViewModel by viewModels()

    override fun getViewBinding(): FragmentDetailWalkBinding =
        FragmentDetailWalkBinding.inflate(layoutInflater)

    override fun initViews() {

    }

    override fun bindViews() {

    }

    override fun observeData() {

    }

    companion object{
        fun newInstance() = DetailWalkFragment()
    }
}