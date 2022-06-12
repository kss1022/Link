package com.example.link.ui.main

import android.content.Context
import android.content.Intent
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.link.R
import com.example.link.databinding.ActivityMainBinding
import com.example.link.ui.base.BaseActivity
import com.example.link.ui.main.detail.DetailFragment
import com.example.link.ui.main.home.HomeFragment
import com.example.link.ui.main.map.MapFragment
import com.example.link.ui.main.my.MyFragment
import com.google.android.material.navigation.NavigationBarView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(), NavigationBarView.OnItemSelectedListener {

    override val viewModel: MainViewModel by viewModels()

    override fun getViewBinding(): ActivityMainBinding =
        ActivityMainBinding.inflate(layoutInflater)

    private val sharedViewModel : MainSharedViewModel by viewModels()

    private val toolbarViewModel : MainToolbarViewModel by viewModels()

    override fun initViews() {
        initToolbar()
        initViewModel()
        binding.bottomNavigationView.setOnItemSelectedListener(this@MainActivity)
        showFragment(HomeFragment.newInstance(), HomeFragment.TAG)
    }




    override fun observeData() {
        toolbarViewModel.navClickEvent.observe(this) {
            showFragment(HomeFragment.newInstance(), HomeFragment.TAG)
        }

        toolbarViewModel.navChangeEvent.observe(this) {
            setToolbar()
        }

        toolbarViewModel.bottomNavigationIsVisible.observe(this){
            it?.let{ setBottomNavigation(it) }
        }

        viewModel.profileImage.observe(this){
            //todo 바깥에 흰색원을 두고 안에 회색원을 두는 방식으로 변경 -> 회색원에 Image 설정
            Glide.with(binding.mainProfileImageView)
                .load(it)
                .circleCrop()
                .into(binding.mainProfileImageView)
        }
    }



    private fun initViewModel() {
        sharedViewModel.fetchData()
    }


    /**
     * Fragment Change
     */


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_home -> {
                showFragment(HomeFragment.newInstance() , HomeFragment.TAG)
                true
            }

            R.id.menu_detail -> {
                showFragment(DetailFragment.newInstance() , DetailFragment.TAG)
                true
            }

            R.id.menu_map -> {
                showFragment(MapFragment.newInstance() , MapFragment.TAG)
                true
            }

            R.id.menu_my -> {
                showFragment(MyFragment.newInstance() , MyFragment.TAG)
                true
            }

            else ->  false
        }
    }


    fun showFragment(fragment: Fragment, tag: String) {
        val findFragment = supportFragmentManager.findFragmentByTag(tag)

        supportFragmentManager.fragments.forEach {
            supportFragmentManager.beginTransaction().hide(it).commitAllowingStateLoss()
        }

        findFragment?.let {
            supportFragmentManager.beginTransaction().show(it).commitAllowingStateLoss()
            it.changeActionBar()
        } ?: kotlin.run {
            supportFragmentManager.beginTransaction().add(R.id.fragmentContainer, fragment, tag)
                .commitAllowingStateLoss()
        }
    }


    private fun Fragment.changeActionBar() {
        when(this){
            is HomeFragment -> this.initActionBar()
            is DetailFragment -> this.initActionBar()
            is MapFragment -> this.initActionBar()
            is MyFragment -> this.initActionBar()
        }
    }


    /**
     * Toolbar Setting
     */

    private fun initToolbar() {
        setSupportActionBar(binding.toolbar)
        binding.toolbar.apply {
            setNavigationOnClickListener { toolbarViewModel.onClick() }
            setOnClickListener { view ->
                Toast.makeText(this@MainActivity, "Toolbar CLick", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun setToolbar() {
        binding.toolbar.apply {
            toolbarViewModel.backgroundColor.value?.let { setBackgroundColor(it) }
            toolbarViewModel.navIconRes.value?.let { setNavigationIcon(it) }
                ?: kotlin.run { navigationIcon = null }
            toolbarViewModel.navIconTint.value?.let { navigationIcon?.setTint(it) }
            toolbarViewModel.titleColor.value?.let { setTitleTextColor(it) }
            toolbarViewModel.visible.value?.let { isVisible = it }
        }

        binding.toolbarTitleTextView.text = toolbarViewModel.title.value
    }

    private fun setBottomNavigation(it: Boolean) {
        binding.bottomNavigationView.isVisible = it
    }



    companion object{
        fun newIntent(context : Context) = Intent(context, MainActivity::class.java)
    }
}