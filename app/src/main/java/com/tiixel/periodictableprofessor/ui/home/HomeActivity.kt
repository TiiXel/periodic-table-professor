package com.tiixel.periodictableprofessor.ui.home

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.tiixel.periodictableprofessor.R
import kotlinx.android.synthetic.main.home_activity.*

class HomeActivity : AppCompatActivity() {

    // View references

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)
        setSupportActionBar(toolbar)

        home_view_pager.adapter = HomePagerAdapter(this, supportFragmentManager)
        home_view_pager_tabs.setupWithViewPager(home_view_pager)
    }
}