package com.example.link.ui.main.detail.fragmentlist

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.link.R
import com.example.link.databinding.FragmentDetailAllBinding
import com.example.link.model.PetModel
import com.example.link.model.RecordModel
import com.example.link.ui.base.BaseFragment
import com.example.link.ui.main.MainSharedViewModel
import com.example.link.ui.main.detail.fragmentlist.DetailAllViewModel.Companion.DAY
import com.example.link.ui.main.detail.fragmentlist.DetailAllViewModel.Companion.MONTH
import com.example.link.ui.main.detail.fragmentlist.DetailAllViewModel.Companion.WEEK
import com.example.link.ui.main.detail.fragmentlist.DetailAllViewModel.Companion.YEAR
import com.example.link.util.ext.toReadableDateString
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
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

        viewLifecycleOwner.lifecycleScope.launch{
            sharedViewModel.updateEndEvent.collect(){
                setRecordData()
            }
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

        val black = ContextCompat.getColor(requireContext(), R.color.text_gray)
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

        setRecordData()
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
        if (pet.isMail)  petMailImageView.setImageDrawable(ContextCompat.getDrawable(requireContext() ,R.drawable.ic_baseline_male_24))
        else   petMailImageView.setImageDrawable(ContextCompat.getDrawable(requireContext() ,R.drawable.ic_baseline_female_24))
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


    /**
     * Set RecordData
     */

    private fun setRecordData() {
        when (viewModel.menu.value) {
            DAY -> {
                setData(sharedViewModel.todayRecord.value!!)
            }
            WEEK -> {
                setWeekData(sharedViewModel.weekRecord.value!!)
            }
            MONTH -> {
                setData(sharedViewModel.monthRecord.value!!)
            }
            YEAR -> {
                setData(sharedViewModel.yearRecord.value!!)
            }
        }
    }


    private fun setData(model: RecordModel) = with(binding) {
        mealCountTextView.text = getString(R.string.count_with_int, model.mealCount)
        mealAmountTextView.text = getString(R.string.amount_with_int, model.meal)
        mealInputPercentTextView.text = model.mealCount.toString()

        snackCountTextView.text = getString(R.string.count_with_int, model.snackCount)
        snackAmountTextView.text = getString(R.string.amount_with_int, model.snack)
        snackInputPercentTextView.text = model.snackCount.toString()

        val m = model.walkTime
        val h = m / 60


        walkCountTextView.text = getString(R.string.count_with_int, model.walkCount)
        walkTimeTextView.text =
            if (h > 0) {
                getString(R.string.hour_and_minutes_with_double, h,  (m - h * 60) )
            } else{
                getString(R.string.minutes_with_double, m)
            }
        walkLengthTextView.text = getString(R.string.km_with_double, model.walkLength)
    }


    private fun setWeekData(models: List<RecordModel>) = with(binding) {

        var mealCountSum = 0
        var mealAmountSum = 0

        var snackCountSum = 0
        var snackAmountSum = 0

        for (i in models) {
            mealCountSum += i.mealCount
            mealAmountSum += i.meal

            snackCountSum += i.snackCount
            snackAmountSum += i.snack
        }

        mealCountTextView.text = getString(R.string.count_with_int, mealCountSum)
        mealAmountTextView.text = getString(R.string.amount_with_int, mealAmountSum)
        mealInputPercentTextView.text = mealCountSum.toString()

        snackCountTextView.text = getString(R.string.count_with_int, snackCountSum)
        snackAmountTextView.text = getString(R.string.amount_with_int, snackAmountSum)
        snackInputPercentTextView.text = snackCountSum.toString()
    }


    companion object {
        fun newInstance() = DetailAllFragment()
    }
}