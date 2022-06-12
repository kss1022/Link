package com.example.link.ui.main.detail.fragmentlist

import android.content.res.ColorStateList
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.fragment.app.viewModels
import com.example.link.R
import com.example.link.databinding.FragmentDetailAllBinding
import com.example.link.databinding.FragmentDetailSleepBinding
import com.example.link.ui.base.BaseFragment
import com.example.link.ui.main.detail.fragmentlist.DetailSleepViewModel.Companion.ANALYSIS
import com.example.link.ui.main.detail.fragmentlist.DetailSleepViewModel.Companion.RECORD
import com.example.link.ui.main.detail.fragmentlist.DetailSleepViewModel.Companion.TODAY
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailSleepFragment : BaseFragment<FragmentDetailSleepBinding, DetailSleepViewModel>(){

    override val viewModel: DetailSleepViewModel by viewModels()

    override fun getViewBinding(): FragmentDetailSleepBinding =
        FragmentDetailSleepBinding.inflate(layoutInflater)

    override fun initViews() {

    }

    override fun bindViews() {
        bindTopMenu()
    }

    override fun observeData() {
        viewModel.menu.observe(viewLifecycleOwner) {
            changeMenu(it)
        }
    }

    /**
     * TopMenuClick
     */

    private var before = TODAY

    private fun bindTopMenu() = with(binding) {
        todayMenuTodayTextView.setOnClickListener { viewModel.setTopMenu(TODAY) }
        topMenuRecordTextView.setOnClickListener { viewModel.setTopMenu(RECORD) }
        topMenuAnalysisTextView.setOnClickListener { viewModel.setTopMenu(ANALYSIS) }
    }


    private fun changeMenu(menu: String) = with(binding) {
        val green = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.green))
        val gray = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.gray_e0))

        val black = ContextCompat.getColor(requireContext(), R.color.text_gray)
        val white = ContextCompat.getColor(requireContext(), R.color.white)

        //이전뷰 변경
        when (before) {
            TODAY -> {
                todayMenuTodayTextView.apply {
                    backgroundTintList = gray
                    setTextColor(black)
                    binding.todayContainer.isGone = true
                }
            }
            RECORD -> {
                topMenuRecordTextView.apply {
                    backgroundTintList = gray
                    setTextColor(black)
                }
                binding.recordContainer.isGone = true
            }
            ANALYSIS -> {
                topMenuAnalysisTextView.apply {
                    backgroundTintList = gray
                    setTextColor(black)
                }
                binding.analysisContainer.isGone = true
            }
        }


        //클릭한 뷰 변경
        when (menu) {
            TODAY -> {
                todayMenuTodayTextView.apply {
                    backgroundTintList = green
                    setTextColor(white)
                }
                binding.todayContainer.visibility = View.VISIBLE
            }
            RECORD -> {
                topMenuRecordTextView.apply {
                    backgroundTintList = green
                    setTextColor(white)
                }
                binding.recordContainer.visibility = View.VISIBLE
            }
            ANALYSIS -> {
                topMenuAnalysisTextView.apply {
                    backgroundTintList = green
                    setTextColor(white)
                }
                binding.analysisContainer.visibility = View.VISIBLE
            }
        }

        before = menu
    }


    companion object{
        fun newInstance() = DetailSleepFragment()
    }
}