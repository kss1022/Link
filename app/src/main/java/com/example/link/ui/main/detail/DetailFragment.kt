package com.example.link.ui.main.detail

import androidx.fragment.app.viewModels
import com.example.link.R
import com.example.link.databinding.FragmentDetailBinding
import com.example.link.ui.base.BaseFragment
import com.example.link.ui.main.MainSharedViewModel
import com.example.link.ui.main.MainToolbarViewModel
import com.example.link.util.lifecycle.SystemUIType
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class DetailFragment : BaseFragment<FragmentDetailBinding, DetailViewModel>() {

    @Inject
    lateinit var sharedViewModel : MainSharedViewModel

    @Inject
    lateinit var toolbarViewModel: MainToolbarViewModel

    override val viewModel: DetailViewModel by viewModels()

    override fun getViewBinding(): FragmentDetailBinding =
        FragmentDetailBinding.inflate(layoutInflater)

    override fun initViews() {
        initActionBar()
    }

    override fun bindViews() {
    }

    override fun observeData() {

    }

    fun initActionBar() {
        toolbarViewModel.setTitle(getString(R.string.app_name))
            .setDefaultNavIcon()
            .onChange()
    }

    companion object{
        fun newInstance()  = DetailFragment()
        const val TAG = "DetailFragment"
    }
}