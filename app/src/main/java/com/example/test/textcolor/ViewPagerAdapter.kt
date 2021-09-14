package com.example.test.textcolor

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

/**
 *
 * @ClassName: ViewPagerAdapter
 */
class ViewPagerAdapter(fm: FragmentManager, private val list: List<Fragment>) : FragmentPagerAdapter(fm) {
    override fun getCount(): Int = list.size

    override fun getItem(position: Int): Fragment = list[position]
}