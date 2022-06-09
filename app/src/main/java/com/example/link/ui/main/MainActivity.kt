package com.example.link.ui.main

import android.content.Context
import android.content.Intent
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import com.example.link.R
import com.example.link.databinding.ActivityMainBinding
import com.example.link.ui.base.BaseActivity
import com.google.android.material.navigation.NavigationBarView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(), NavigationBarView.OnItemSelectedListener {

    override val viewModel: MainViewModel by viewModels()

    override fun getViewBinding(): ActivityMainBinding =
        ActivityMainBinding.inflate(layoutInflater)



    override fun initViews() {
        binding.bottomNavigationView.setOnItemSelectedListener(this@MainActivity)

//        showFragment(HomeFragment.newInstance(), HomeFragment.TAG)
    }

     fun showFragment(fragment: Fragment, tag: String) {
        val findFragment = supportFragmentManager.findFragmentByTag(tag)

        supportFragmentManager.fragments.forEach {
            supportFragmentManager.beginTransaction().hide(it).commitAllowingStateLoss()
        }

        findFragment?.let {
            supportFragmentManager.beginTransaction().show(it).commitAllowingStateLoss()
        } ?: kotlin.run {
            supportFragmentManager.beginTransaction().add(R.id.fragmentContainer, fragment, tag)
                .commitAllowingStateLoss()
        }

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_home -> {
//                showFragment(HomeFragment.newInstance() , HomeFragment.TAG)
                true
            }

            R.id.menu_detail -> {
//                showFragment(TimeTableFragment.newInstance() , TimeTableFragment.TAG)
                true
            }

            R.id.menu_map -> {
//                showFragment(MemoFragment.newInstance() , MemoFragment.TAG)
                true
            }

            R.id.menu_my -> {
//                showFragment(MemoFragment.newInstance() , MemoFragment.TAG)
                true
            }

            else ->  false
        }
    }


    companion object{
        fun newIntent(context : Context) = Intent(context, MainActivity::class.java)
    }


}