package com.example.link.ui.main.map

import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.example.link.R
import com.example.link.databinding.FragmentMapBinding
import com.example.link.ui.base.BaseFragment
import com.example.link.ui.main.MainSharedViewModel
import com.example.link.ui.main.MainToolbarViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MapFragment : BaseFragment<FragmentMapBinding, MapViewModel>() {

    @Inject
    lateinit var sharedViewModel: MainSharedViewModel

    @Inject
    lateinit var toolbarViewModel: MainToolbarViewModel

    override val viewModel: MapViewModel by viewModels()

    override fun getViewBinding(): FragmentMapBinding =
        FragmentMapBinding.inflate(layoutInflater)

    override fun initViews() {
        initActionBar()
        showBottomSheet()
    }


    override fun bindViews() {
    }

    override fun observeData() {

    }

    fun initActionBar() {
        toolbarViewModel.setTitle(getString(R.string.app_name))
            .setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
            .setTitleColor(ContextCompat.getColor(requireContext(), R.color.green))
            .setDefaultNavIcon()
            .onChange()
        toolbarViewModel.onChangeBottomNavigation(false)
    }

    private fun showBottomSheet() {
        MapBottomSheetFragment.newInstance()
            .show(requireActivity().supportFragmentManager, MapBottomSheetFragment.TAG)
    }

    companion object {
        fun newInstance() = MapFragment()
        const val TAG = "MapFragment"
    }
}