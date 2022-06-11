package com.example.link.widget.adapter.fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.link.ui.base.BaseFragment

class DetailFragmentAdapter(
    fragment : Fragment,
    val fragmentList : List<Fragment>
) : FragmentStateAdapter(fragment){


    override fun getItemCount(): Int = fragmentList.size

    override fun createFragment(position: Int): Fragment = fragmentList[position]
}