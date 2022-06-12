package com.example.link.ui.main.detail.fragmentlist

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Rect
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.link.R
import com.example.link.databinding.FragmentDetailMealBinding
import com.example.link.model.EatMemoModel
import com.example.link.model.PetModel
import com.example.link.ui.base.BaseFragment
import com.example.link.ui.main.MainSharedViewModel
import com.example.link.ui.main.detail.fragmentlist.DetailMealViewModel.Companion.DAY
import com.example.link.ui.main.detail.fragmentlist.DetailMealViewModel.Companion.MONTH
import com.example.link.ui.main.detail.fragmentlist.DetailMealViewModel.Companion.WEEK
import com.example.link.ui.main.detail.fragmentlist.DetailMealViewModel.Companion.YEAR
import com.example.link.util.ext.fromDpToPx
import com.example.link.util.ext.toReadableDateString
import com.example.link.util.resource.ResourceProvider
import com.example.link.widget.adapter.model.ModelRecyclerViewAdapter
import com.example.link.widget.adapter.model.listener.MemoModelAdapterListener
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


    private val memoAdapter by lazy {
        ModelRecyclerViewAdapter<EatMemoModel>(
            modelList = listOf(),
            resourcesProvider = resourceProvider,
            adapterListener = object : MemoModelAdapterListener {
                override fun click() {
                    Toast.makeText(requireContext(), "memo Click", Toast.LENGTH_SHORT).show()
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
        fun newInstance() = DetailMealFragment()
    }
}