package com.example.link.ui.main.detail.fragmentlist

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.example.link.R
import com.example.link.databinding.FragmentDetailAllBinding
import com.example.link.model.PetModel
import com.example.link.ui.base.BaseFragment
import com.example.link.ui.main.MainSharedViewModel
import com.example.link.ui.main.detail.fragmentlist.DetailAllViewModel.Companion.DAY
import com.example.link.ui.main.detail.fragmentlist.DetailAllViewModel.Companion.MONTH
import com.example.link.ui.main.detail.fragmentlist.DetailAllViewModel.Companion.WEEK
import com.example.link.ui.main.detail.fragmentlist.DetailAllViewModel.Companion.YEAR
import com.example.link.util.ext.toReadableDateString
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class DetailAllFragment : BaseFragment<FragmentDetailAllBinding, DetailAllViewModel>() {

    @Inject
    lateinit var sharedViewModel: MainSharedViewModel

    override val viewModel: DetailAllViewModel by viewModels()

    override fun getViewBinding(): FragmentDetailAllBinding =
        FragmentDetailAllBinding.inflate(layoutInflater)

    override fun initViews() {

    }

    override fun bindViews() {
        bindTopMenu()
    }


    override fun observeData() {
        viewModel.menu.observe(viewLifecycleOwner) {
            changeMenu(it)
        }

        sharedViewModel.petModel.observe(viewLifecycleOwner) {
            setPetData(it)
        }
    }


    /**
     * TopMenuClick
     */

    private var before = DAY

    private fun bindTopMenu() = with(binding) {
        topMenuDayTextView.setOnClickListener { viewModel.setTopMenu(DAY) }
        topMenuMonthTextView.setOnClickListener { viewModel.setTopMenu(MONTH) }
        topMenuWeekTextView.setOnClickListener { viewModel.setTopMenu(WEEK) }
        topMenuYearTextView.setOnClickListener { viewModel.setTopMenu(YEAR) }
    }


    private fun changeMenu(menu: String) = with(binding) {
        val green = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.green))
        val gray = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.gray_e0))

        val black = ContextCompat.getColor(requireContext(), R.color.black)
        val white = ContextCompat.getColor(requireContext(), R.color.white)

        //이전뷰 변경
        when (before) {
            DAY -> {
                topMenuDayTextView.apply {
                    backgroundTintList = gray
                    setTextColor(black)
                }
            }
            MONTH -> {
                topMenuMonthTextView.apply {
                    backgroundTintList = gray
                    setTextColor(black)
                }
            }
            WEEK -> {
                topMenuWeekTextView.apply {
                    backgroundTintList = gray
                    setTextColor(black)
                }
            }
            YEAR -> {
                topMenuYearTextView.apply {
                    backgroundTintList = gray
                    setTextColor(black)
                }
            }
        }


        //클릭한 뷰 변경
        when (menu) {
            DAY -> {
                topMenuDayTextView.apply {
                    backgroundTintList = green
                    setTextColor(white)
                }
            }
            MONTH -> {
                topMenuMonthTextView.apply {
                    backgroundTintList = green
                    setTextColor(white)
                }
            }
            WEEK -> {
                topMenuWeekTextView.apply {
                    backgroundTintList = green
                    setTextColor(white)
                }
            }
            YEAR -> {
                topMenuYearTextView.apply {
                    backgroundTintList = green
                    setTextColor(white)
                }
            }
        }

        before = menu
    }


    /**
     * Set Data
     */

    @SuppressLint("SetTextI18n")
    private fun setPetData(pet: PetModel) = with(binding) {
        petNameTextView.text = pet.name
        petYearTextView.text = getYear(pet.birthDay)
        petTypeTextView.text = pet.type
        petMailTextView.text =
            if (pet.isMail) getString(R.string.character_mail) else getString(R.string.character_femail)
        petWeightTextView.text = "${pet.weight}kg"
    }


    private fun getYear(date: List<Int>): String {
        val today = Date(System.currentTimeMillis()).toReadableDateString().split(".")

        var year = today[0].toInt() - date[0]
        var month = today[1].toInt() - date[1]
        val day = today[2].toInt() - date[2]


        if (day < 0) {
            month--
        }
        if (month < 0) {
            year--
            month += 12
        }

        return getString(R.string.pet_age, year, month)
    }

    companion object {
        fun newInstance() = DetailAllFragment()
    }
}