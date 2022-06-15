package com.example.link.ui.main.home

import android.graphics.Rect
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.link.R
import com.example.link.databinding.FragmentHomeBinding
import com.example.link.model.MemoModel
import com.example.link.model.PetModel
import com.example.link.ui.base.BaseFragment
import com.example.link.ui.main.MainActivity
import com.example.link.ui.main.MainSharedViewModel
import com.example.link.ui.main.MainToolbarViewModel
import com.example.link.util.ext.fromDpToPx
import com.example.link.util.ext.toReadableDateString
import com.example.link.util.lifecycle.SingleLiveEvent
import com.example.link.util.lifecycle.SystemUIType
import com.example.link.util.resource.ResourceProvider
import com.example.link.widget.adapter.model.ModelRecyclerViewAdapter
import com.example.link.widget.adapter.model.listener.MemoModelAdapterListener
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {

    @Inject
    lateinit var sharedViewModel: MainSharedViewModel

    @Inject
    lateinit var toolbarViewModel: MainToolbarViewModel

    @Inject
    lateinit var resourceProvider: ResourceProvider


    override val viewModel: HomeViewModel by viewModels()

    override fun getViewBinding(): FragmentHomeBinding =
        FragmentHomeBinding.inflate(layoutInflater)

    private val clickRecord = SingleLiveEvent<Unit>()

    private val memoAdapter by lazy{
        ModelRecyclerViewAdapter<MemoModel>(
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
        initActionBar()
        initViewModel()
        initViewPager()
    }


    override fun bindViews() {
        binding.mainPetImageView.setOnClickListener {
            clickRecord.call()
        }
    }

    override fun observeData() {
        sharedViewModel.petModel.observe(viewLifecycleOwner) {
            setPetData(it)
        }

        viewModel.memoList.observe(viewLifecycleOwner) {
            memoAdapter.submitList(it)
        }

        clickRecord.observe(viewLifecycleOwner){
            (requireActivity() as MainActivity).showFragment(HomeRecordFragment.newInstance(), HomeRecordFragment.TAG)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding.memoViewPager.unregisterOnPageChangeCallback(viewPagerOnPageChangeCallback)
    }

    fun initActionBar() {
        toolbarViewModel.setTitle(getString(R.string.app_name))
            .setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
            .setTitleColor(ContextCompat.getColor(requireContext(), R.color.green))
            .setNavIconRes(null)
            .onChange()

        toolbarViewModel.onChangeBottomNavigation(true)
    }

    private fun initViewModel() {
        viewModel.fetchData()
    }




    /**
     * ViewPager
     */

    private val viewPagerOnPageChangeCallback by lazy {
        object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                setIndicator(position)
            }
        }
    }



    private fun initViewPager() {
        binding.memoViewPager.apply {
            adapter = memoAdapter

            //양 옆에 미리보기
            val currentVisibleItemPx = 30f.fromDpToPx()
            val nextVisibleItemPx = 10.fromDpToPx()
            val pageTranslationX = nextVisibleItemPx + currentVisibleItemPx


            addItemDecoration(object: RecyclerView.ItemDecoration() {
                override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                    outRect.right = currentVisibleItemPx
                    outRect.left = currentVisibleItemPx
                }
            })
            offscreenPageLimit = 1
            setPageTransformer { page, position ->
                page.translationX = -pageTranslationX * ( position)
            }

            registerOnPageChangeCallback(viewPagerOnPageChangeCallback)
        }
    }

    private fun setIndicator(position: Int) {
            when(position){
                0->{
                    binding.indicatorDot2.background = resourceProvider.getDrawable(R.drawable.dot_gray)
                }

                1->{
                    binding.indicatorDot2.background = resourceProvider.getDrawable(R.drawable.dot_green)
                    binding.indicatorDot3.background = resourceProvider.getDrawable(R.drawable.dot_gray)
                }

                2->{
                    binding.indicatorDot3.background = resourceProvider.getDrawable(R.drawable.dot_green)
                }
            }
    }


    /**
     * Set Data
     */

    private fun setPetData(pet: PetModel) = with(binding) {
        petNameTextView.text = pet.name
        petYearTextView.text = getYear(pet.birthDay)
        petTypeTextView.text = pet.type

        if (pet.isMail) petMailImageView.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_male_24))
        else petMailImageView.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_female_24))
        petRecordTitleTextView.text = getString(R.string.pet_record_title, pet.name)

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
        fun newInstance() = HomeFragment()
        const val TAG = "HomeFragment"
    }
}