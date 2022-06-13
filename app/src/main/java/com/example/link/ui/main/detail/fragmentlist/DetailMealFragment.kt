package com.example.link.ui.main.detail.fragmentlist

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.res.ColorStateList
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.link.R
import com.example.link.databinding.FragmentDetailMealBinding
import com.example.link.model.EatMemoModel
import com.example.link.model.PetModel
import com.example.link.model.RecordModel
import com.example.link.ui.base.BaseFragment
import com.example.link.ui.main.MainSharedViewModel
import com.example.link.ui.main.detail.fragmentlist.DetailMealViewModel.Companion.DAY
import com.example.link.ui.main.detail.fragmentlist.DetailMealViewModel.Companion.MONTH
import com.example.link.ui.main.detail.fragmentlist.DetailMealViewModel.Companion.WEEK
import com.example.link.ui.main.detail.fragmentlist.DetailMealViewModel.Companion.YEAR
import com.example.link.util.ext.fromDpToPx
import com.example.link.util.ext.toReadableDateString
import com.example.link.util.lifecycle.SingleLiveEvent
import com.example.link.util.resource.ResourceProvider
import com.example.link.widget.adapter.model.ModelRecyclerViewAdapter
import com.example.link.widget.adapter.model.listener.EditModelAdapterListener
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class DetailMealFragment : BaseFragment<FragmentDetailMealBinding, DetailMealViewModel>() {

    @Inject
    lateinit var sharedViewModel: MainSharedViewModel

    @Inject
    lateinit var resourceProvider: ResourceProvider

    override val viewModel: DetailMealViewModel by viewModels()

    override fun getViewBinding(): FragmentDetailMealBinding =
        FragmentDetailMealBinding.inflate(layoutInflater)

    private val clickEditMemo = SingleLiveEvent<EatMemoModel>(300)


    private val memoAdapter by lazy {
        ModelRecyclerViewAdapter<EatMemoModel>(
            modelList = listOf(),
            resourcesProvider = resourceProvider,
            adapterListener = object : EditModelAdapterListener {
                override fun clickEditButton(editModel: EatMemoModel) {
                    clickEditMemo.value = editModel
                }
            }
        )
    }

    override fun initViews() {
        initViewModel()
        initViewPager()
    }

    override fun bindViews() {
        bindTopMenu()
    }

    override fun observeData() {
        viewModel.memoList.observe(viewLifecycleOwner) {
            memoAdapter.submitList(it)
        }

        viewModel.menu.observe(viewLifecycleOwner) {
            changeMenu(it)
        }

        sharedViewModel.petModel.observe(viewLifecycleOwner) {
            setPetData(it)
        }

        clickEditMemo.observe(viewLifecycleOwner) { memo ->
            memo?.let { showEditDialog(it) }
        }

        sharedViewModel.todayRecord.observe(viewLifecycleOwner) {
            setRecordData()
        }
    }


    private fun initViewModel() {
        viewModel.fetchData()
    }


    /**
     * ViewPager
     */


    private fun initViewPager() {
        binding.viewPager.apply {
            adapter = memoAdapter

            //양 옆에 미리보기
            val currentVisibleItemPx = 30f.fromDpToPx()
            val nextVisibleItemPx = 10.fromDpToPx()
            val pageTranslationX = nextVisibleItemPx + currentVisibleItemPx


            addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    outRect.right = currentVisibleItemPx
                    outRect.left = currentVisibleItemPx
                }
            })
            offscreenPageLimit = 1
            setPageTransformer { page, position ->
                page.translationX = -pageTranslationX * (position)
            }
        }
    }

    /**
     * TopMenuClick
     */

    private var before = DetailMealViewModel.DAY

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
            WEEK -> {
                topMenuWeekTextView.apply {
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

                binding.viewPagerContainer.visibility = View.VISIBLE
                binding.mealGraphLayout.visibility = View.INVISIBLE
            }
            WEEK -> {
                topMenuWeekTextView.apply {
                    backgroundTintList = green
                    setTextColor(white)
                }
                binding.viewPagerContainer.visibility = View.INVISIBLE
                binding.mealGraphLayout.visibility = View.VISIBLE
            }
            MONTH -> {
                topMenuMonthTextView.apply {
                    backgroundTintList = green
                    setTextColor(white)
                }
                binding.viewPagerContainer.visibility = View.VISIBLE
                binding.mealGraphLayout.visibility = View.INVISIBLE
            }
            YEAR -> {
                topMenuYearTextView.apply {
                    backgroundTintList = green
                    setTextColor(white)
                }
                binding.viewPagerContainer.visibility = View.VISIBLE
                binding.mealGraphLayout.visibility = View.INVISIBLE
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


    /**
     * Set RecordData
     */

    private fun setRecordData() {
        when (viewModel.menu.value!!) {
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
        eatingCountTextView.text = getString(R.string.count_with_int, model.mealCount)
        eatingAmountTextView.text = getString(R.string.amount_with_int, model.meal)

        snackCountTextView.text = getString(R.string.count_with_int, model.snackCount)
        snackAmountTextView.text = getString(R.string.amount_with_int, model.snack)
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

        eatingCountTextView.text = getString(R.string.count_with_int, mealCountSum)
        eatingAmountTextView.text = getString(R.string.amount_with_int, mealAmountSum)



        snackCountTextView.text = getString(R.string.count_with_int, snackCountSum)
        snackAmountTextView.text = getString(R.string.amount_with_int, snackAmountSum)
    }




    /**
     *  Edit AlertDialog
     */


    private fun showEditDialog(model: EatMemoModel) {
        val dialog = AlertDialog.Builder(requireContext()).create()
        val editMealAlertDialog = LayoutInflater.from(requireContext())
            .inflate(R.layout.alert_dialog_edit_meal, null)

        val eatTypeEditText = editMealAlertDialog.findViewById<EditText>(R.id.eatTypeEditText)
        val eatAmountEditText = editMealAlertDialog.findViewById<EditText>(R.id.eatAmountEditText)
        val memoEditText = editMealAlertDialog.findViewById<EditText>(R.id.memoEditText)

        eatTypeEditText.setText(model.eatType)
        eatAmountEditText.setText(model.eatAmount)
        memoEditText.setText(model.memo)

        editMealAlertDialog.findViewById<TextView>(R.id.positiveButton).setOnClickListener {
            val type = eatTypeEditText.text.toString()
            val amount = eatAmountEditText.text.toString()
            val memo = memoEditText.text.toString()
            viewModel.editMemo(model, type, amount, memo)
            dialog.dismiss()
        }

        editMealAlertDialog.findViewById<TextView>(R.id.negativeButton).setOnClickListener {
            dialog.dismiss()
        }

        dialog.apply {
            setView(editMealAlertDialog)
        }.show()
    }


    companion object {
        fun newInstance() = DetailMealFragment()
    }
}