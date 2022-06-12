package com.example.link.ui.main.detail.fragmentlist

import android.content.res.ColorStateList
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.example.link.R
import com.example.link.databinding.FragmentDetailWalkBinding
import com.example.link.ui.base.BaseFragment
import com.example.link.ui.main.MainSharedViewModel
import com.example.link.ui.main.MainToolbarViewModel
import com.example.link.ui.main.detail.fragmentlist.DetailWalkViewModel.Companion.ANALYSIS
import com.example.link.ui.main.detail.fragmentlist.DetailWalkViewModel.Companion.GPS
import com.example.link.ui.main.detail.fragmentlist.DetailWalkViewModel.Companion.STEP
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class DetailWalkFragment : BaseFragment<FragmentDetailWalkBinding, DetailWalkViewModel>(){

    @Inject
    lateinit var sharedViewModel: MainSharedViewModel

    @Inject
    lateinit var toolbarViewModel: MainToolbarViewModel

    override val viewModel: DetailWalkViewModel by viewModels()

    override fun getViewBinding(): FragmentDetailWalkBinding =
        FragmentDetailWalkBinding.inflate(layoutInflater)

    override fun initViews() {

    }

    override fun bindViews() {
        bindTopMenu()
        bindButton()
    }



    override fun observeData() {
        viewModel.menu.observe(viewLifecycleOwner) {
            changeMenu(it)
        }

        viewModel.isWalk.observe(viewLifecycleOwner){ isWalk->
            isWalk?.let { setWalkButton(it)  }
        }


        viewModel.isBottomViewVisible.observe(viewLifecycleOwner){ isVisible->
            isVisible?.let { setBottomView(it)  }
        }
    }



    /**
     * TopMenuClick
     */

    private var before = STEP

    private fun bindTopMenu() = with(binding) {
        topMenuStepTextView.setOnClickListener { viewModel.setTopMenu(STEP) }
        topMenuGpsTextView.setOnClickListener { viewModel.setTopMenu(GPS) }
        topMenuAnalysisTextView.setOnClickListener { viewModel.setTopMenu(ANALYSIS) }
    }


    private fun changeMenu(menu: String) = with(binding) {
        val green = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.green))
        val gray = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.gray_e0))

        val black = ContextCompat.getColor(requireContext(), R.color.text_gray)
        val white = ContextCompat.getColor(requireContext(), R.color.white)

        //이전뷰 변경
        when (before) {
            STEP -> {
                topMenuStepTextView.apply {
                    backgroundTintList = gray
                    setTextColor(black)
                }
                binding.stepContainer.isGone = true
            }
            GPS -> {
                topMenuGpsTextView.apply {
                    backgroundTintList = gray
                    setTextColor(black)
                }
                binding.mapSampleImageView.isGone = true
                binding.walkButton.isGone = true
                binding.bottomWalkDataButton.isGone = true
            }
            ANALYSIS -> {
                topMenuAnalysisTextView.apply {
                    backgroundTintList = gray
                    setTextColor(black)
                }
            }
        }


        //클릭한 뷰 변경
        when (menu) {
            STEP -> {
                topMenuStepTextView.apply {
                    backgroundTintList = green
                    setTextColor(white)
                }
                binding.stepContainer.visibility = View.VISIBLE
                toolbarViewModel.onChangeBottomNavigation(true)
                toolbarViewModel.setGps(false)
            }
            GPS -> {
                topMenuGpsTextView.apply {
                    backgroundTintList = green
                    setTextColor(white)
                }
                binding.walkButton.visibility = View.VISIBLE
                binding.mapSampleImageView.visibility = View.VISIBLE
                binding.bottomWalkDataButton.visibility = View.VISIBLE
                toolbarViewModel.onChangeBottomNavigation(false)
                toolbarViewModel.setGps(true)
            }
            ANALYSIS -> {
                topMenuAnalysisTextView.apply {
                    backgroundTintList = green
                    setTextColor(white)
                }
            }
        }

        before = menu
    }




    private fun bindButton() {
        binding.walkButton.setOnClickListener {
            viewModel.clickWalkButton()
        }

        binding.bottomWalkDataButton.setOnClickListener {
            viewModel.clickBottomButton()

        }
    }

    private fun setWalkButton(it: Boolean) {
        if(it){
            binding.walkButton.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.walk_pause_button))
            Toast.makeText(requireContext(), "걷기를 시작합니다!!!", Toast.LENGTH_SHORT).show()
        }else{
            binding.walkButton.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.walk_start_button))
            Toast.makeText(requireContext(), "걷기를 중단합니다!!!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setBottomView(it: Boolean) {
        binding.bottomWalkDataContainer.isVisible = it

        if(it){
            binding.bottomWalkDataButton.apply {
                val lp = layoutParams as ConstraintLayout.LayoutParams
                lp.bottomToBottom =ConstraintLayout.LayoutParams.UNSET
                lp.bottomToTop = binding.bottomWalkDataContainer.id
                layoutParams = lp
                setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_keyboard_arrow_down_24))
            }
        }else{
            binding.bottomWalkDataButton.apply {
                val lp = layoutParams as ConstraintLayout.LayoutParams
                lp.bottomToBottom = binding.mapSampleImageView.id
                lp.bottomToTop = ConstraintLayout.LayoutParams.UNSET
                layoutParams = lp
                setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_keyboard_arrow_up_24))
            }
        }
    }



    companion object{
        fun newInstance() = DetailWalkFragment()
    }
}