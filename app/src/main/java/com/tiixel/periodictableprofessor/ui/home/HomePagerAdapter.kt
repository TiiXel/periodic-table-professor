package com.tiixel.periodictableprofessor.ui.home

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.tiixel.periodictableprofessor.R
import com.tiixel.periodictableprofessor.ui.home.learn.LearnFragment

class HomePagerAdapter(val context: Context, fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment? {
        return when (position) {
            0 -> LearnFragment()
            1 -> Fragment()
            else -> null
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> context.resources.getString(R.string.home_tab_learn_title)
            1 -> context.resources.getString(R.string.home_tab_statistics_title)
            else -> null
        }
    }
}