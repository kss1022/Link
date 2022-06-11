package com.example.link.ui.main.detail

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.link.R
import com.example.link.databinding.FragmentDetailBinding
import com.example.link.ui.base.BaseFragment
import com.example.link.ui.main.MainSharedViewModel
import com.example.link.ui.main.MainToolbarViewModel
import com.example.link.ui.main.detail.fragmentlist.*
import com.example.link.widget.adapter.fragment.DetailFragmentAdapter
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class DetailFragment : BaseFragment<FragmentDetailBinding, DetailViewModel>() {

    @Inject
    lateinit var sharedViewModel: MainSharedViewModel

    @Inject
    lateinit var toolbarViewModel: MainToolbarViewModel

    override val viewModel: DetailViewModel by viewModels()

    override fun getViewBinding(): FragmentDetailBinding =
        FragmentDetailBinding.inflate(layoutInflater)


    private lateinit var viewPagerAdapter: DetailFragmentAdapter


    override fun initViews() {
        initActionBar()
        initTabBar()
    }


    override fun bindViews() {
    }

    override fun observeData() {

    }

    fun initActionBar() {
        toolbarViewModel.setTitle(getString(R.string.app_name))
            .setDefaultNavIcon()
            .onChange()

        toolbarViewModel.bottomNavigationIsShow.value = true
    }

    private fun initTabBar() = with(binding) {
        val menuCategories = DetailMenuCategory.values()

        if (::viewPagerAdapter.isInitialized.not()) {

            val fragmentList = listOf<Fragment>(
                DetailAllFragment.newInstance(), DetailMealFragment.newInstance(),
                DetailSleepFragment.newInstance(), DetailWalkFragment.newInstance()
            )


            viewPagerAdapter = DetailFragmentAdapter(
                this@DetailFragment,
                fragmentList
            )

            viewPager.adapter = viewPagerAdapter
            viewPager.offscreenPageLimit = menuCategories.size

            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.setText(menuCategories[position].categoryNameId)
            }.attach()
        }
    }

    companion object {
        fun newInstance() = DetailFragment()
        const val TAG = "DetailFragment"
    }
}