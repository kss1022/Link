package com.example.link.ui.main.detail.fragmentlist

import android.content.res.ColorStateList
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.fragment.app.viewModels
import com.example.link.R
import com.example.link.databinding.FragmentDetailSleepBinding
import com.example.link.ui.base.BaseFragment
import com.example.link.ui.main.MainSharedViewModel
import com.example.link.ui.main.detail.fragmentlist.DetailSleepViewModel.Companion.ANALYSIS
import com.example.link.ui.main.detail.fragmentlist.DetailSleepViewModel.Companion.RECORD
import com.example.link.ui.main.detail.fragmentlist.DetailSleepViewModel.Companion.TODAY
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DetailSleepFragment : BaseFragment<FragmentDetailSleepBinding, DetailSleepViewModel>(){

    @Inject
    lateinit var sharedViewModel: MainSharedViewModel


    override val viewModel: DetailSleepViewModel by viewModels()

    override fun getViewBinding(): FragmentDetailSleepBinding =
        FragmentDetailSleepBinding.inflate(layoutInflater)

    override fun initViews() {

    }

    override fun bindViews() {
        bindTopMenu()
        binding.totalSleepProgressBar.setOnClickListener { sharedViewModel.getSleepData() }
    }

    override fun observeData() {
        viewModel.menu.observe(viewLifecycleOwner) {
            changeMenu(it)
        }

        sharedViewModel.heartBeat.observe(viewLifecycleOwner){
            setSleepData(it)
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

    private fun setSleepData(data: Int) = with(binding){
        binding.totalProgressTextView.text = data.toString()


        when {
            data > 90 -> {
                qualityCountTextView.text = "10"
                (binding.qualityBarGreenView.layoutParams as ConstraintLayout.LayoutParams)
                    .matchConstraintPercentWidth = 0.1f
                binding.totalSleepProgressBar.progress = 90
                heartBeatCheckView.progress = 90
            }
            data >80 -> {
                qualityCountTextView.text = "30"
                (binding.qualityBarGreenView.layoutParams as ConstraintLayout.LayoutParams)
                    .matchConstraintPercentWidth = 0.3f
                binding.totalSleepProgressBar.progress = 80
                heartBeatCheckView.progress = 80
            }
            data >70 -> {
                qualityCountTextView.text = "50"
                (binding.qualityBarGreenView.layoutParams as ConstraintLayout.LayoutParams)
                    .matchConstraintPercentWidth = 0.5f
                binding.totalSleepProgressBar.progress = 60
                heartBeatCheckView.progress = 70
            }
            data >60 -> {
                qualityCountTextView.text = "70"
                (binding.qualityBarGreenView.layoutParams as ConstraintLayout.LayoutParams)
                    .matchConstraintPercentWidth = 0.7f

                binding.totalSleepProgressBar.progress = 50
            }
            data >50 -> {
                qualityCountTextView.text = "90"
                (binding.qualityBarGreenView.layoutParams as ConstraintLayout.LayoutParams)
                    .matchConstraintPercentWidth = 0.9f
                binding.totalSleepProgressBar.progress = 30
            }
            else -> {
                qualityCountTextView.text = "100"
                (binding.qualityBarGreenView.layoutParams as ConstraintLayout.LayoutParams)
                    .matchConstraintPercentWidth = 1f
                binding.totalSleepProgressBar.progress = 10
            }
        }

        binding.qualityBarGreenView.requestLayout()
    }



    companion object{
        fun newInstance() = DetailSleepFragment()
    }
}