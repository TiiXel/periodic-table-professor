package com.tiixel.periodictableprofessor.ui.home

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.mikepenz.aboutlibraries.Libs
import com.mikepenz.aboutlibraries.LibsBuilder
import com.mikepenz.iconics.context.IconicsContextWrapper
import com.mikepenz.iconics.utils.IconicsMenuInflaterUtil
import com.tiixel.periodictableprofessor.R
import kotlinx.android.synthetic.main.home_activity.*

class HomeActivity : AppCompatActivity() {

    //<editor-fold desc="Life cycle methods">
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)
        setSupportActionBar(toolbar)

        home_view_pager.adapter = HomePagerAdapter(this, supportFragmentManager)
        home_view_pager_tabs.setupWithViewPager(home_view_pager)
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(IconicsContextWrapper.wrap(newBase))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        IconicsMenuInflaterUtil.inflate(menuInflater, this, R.menu.home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home_menu_about -> {
                LibsBuilder()
                    .withActivityTheme(R.style.AppTheme)
                    .withActivityStyle(Libs.ActivityStyle.LIGHT_DARK_TOOLBAR)
                    .withActivityTitle(getString(R.string.app_name))
                    .withAboutAppName(getString(R.string.app_name))
                    .withAboutDescription(getString(R.string.app_description))
                    .withAboutIconShown(true)
                    .withAboutVersionShown(true)
                    .withLicenseShown(true)
                    .start(this)
            }
        }
        return true
    }
    //</editor-fold>
}