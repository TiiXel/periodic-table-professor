package com.tiixel.periodictableprofessor.ui

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import com.mikepenz.aboutlibraries.Libs
import com.mikepenz.aboutlibraries.LibsBuilder
import com.mikepenz.aboutlibraries.LibsConfiguration
import com.mikepenz.aboutlibraries.entity.Library
import com.mikepenz.aboutlibraries.ui.LibsFragment
import com.mikepenz.google_material_typeface_library.GoogleMaterial
import com.mikepenz.iconics.IconicsDrawable
import com.tiixel.periodictableprofessor.R
import com.tiixel.periodictableprofessor.ui.elementlist.ElementTableFragment
import com.tiixel.periodictableprofessor.ui.statistics.StatisticsFragment
import com.tiixel.periodictableprofessor.ui.study.StudyFragment
import com.tiixel.periodictableprofessor.ui.tutorial.Tutorial
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.main_layout_drawer.*

class MainActivity : AppCompatActivity() {

    private val defaultFragmentClass = StudyFragment::class.java
    lateinit var activeFragmentClass: Class<out Any>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Show tutorial
        val sharedPref = getSharedPreferences(Tutorial.tutorialSharedPreferencesFile, Context.MODE_PRIVATE)
        val tutorialCompleted = sharedPref.getBoolean(Tutorial.tutorialCompletedSharedPreferencesKey, false)
        if (!tutorialCompleted) startActivity(Intent(this, Tutorial::class.java))

        AndroidInjection.inject(this)

        setContentView(R.layout.main_layout_drawer)

        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(
                IconicsDrawable(this@MainActivity)
                    .icon(GoogleMaterial.Icon.gmd_menu)
                    .color(Color.WHITE)
                    .sizeDp(20)
            )
        }

        main_layout_drawer.setNavigationItemSelectedListener { menuItem ->

            if (menuItem.itemId == R.id.nav_tutorial) startActivity(Intent(this, Tutorial::class.java))
            else selectDrawerItem(menuItem)

            return@setNavigationItemSelectedListener true
        }

        val savedActiveFragment = savedInstanceState?.getString("active_fragment_class")?.let { Class.forName(it) }
        replaceFragment(defaultFragmentClass)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("active_fragment_class", activeFragmentClass.name)
        super.onSaveInstanceState(outState)
    }

    override fun onSupportNavigateUp(): Boolean {
        drawer_layout.openDrawer(GravityCompat.START)
        return true
    }

    private fun selectDrawerItem(menuItem: MenuItem) {

        val fragmentClass = when (menuItem.itemId) {
            R.id.nav_study -> StudyFragment::class.java
            R.id.nav_statistics -> StatisticsFragment::class.java
            R.id.nav_periodic_table -> ElementTableFragment::class.java
            R.id.nav_about -> LibsFragment::class.java
            else -> defaultFragmentClass
        }

        replaceFragment(fragmentClass)

        menuItem.isChecked = true
        title = if (fragmentClass == defaultFragmentClass) getString(R.string.app_name) else menuItem.title

        drawer_layout.closeDrawers()
    }

    private fun replaceFragment(fragmentClass: Class<out Any>) {

        activeFragmentClass = fragmentClass

        val fragment =
            if (fragmentClass == LibsFragment::class.java) getAboutFragment()
            else fragmentClass.newInstance() as Fragment

        supportFragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit()
    }

    private fun getAboutFragment() : Fragment {

        val libsListener = object : LibsConfiguration.LibsListener {

            override fun onExtraClicked(v: View?, specialButton: Libs.SpecialButton?): Boolean {
                when (specialButton) {
                    Libs.SpecialButton.SPECIAL1 -> {
                        val appPackageName = packageName
                        try {
                            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName")))
                        } catch (e: ActivityNotFoundException) {
                            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")))
                        }
                        return true
                    }
                    Libs.SpecialButton.SPECIAL2 -> {
                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://https://github.com/TiiXel/periodic-table-professor")))
                        return true
                    }
                    else -> return false
                }
            }

            override fun onIconClicked(v: View?) {
            }

            override fun onLibraryBottomClicked(v: View?, library: Library?): Boolean {
                return false
            }

            override fun onLibraryAuthorClicked(v: View?, library: Library?): Boolean {
                return false
            }

            override fun onLibraryContentClicked(v: View?, library: Library?): Boolean {
                return false
            }

            override fun onIconLongClicked(v: View?): Boolean {
                return false
            }

            override fun onLibraryAuthorLongClicked(v: View?, library: Library?): Boolean {
                return false
            }

            override fun onLibraryBottomLongClicked(v: View?, library: Library?): Boolean {
                return false
            }

            override fun onLibraryContentLongClicked(v: View?, library: Library?): Boolean {
                return false
            }
        }

        return LibsBuilder()
            .withActivityTheme(R.style.AppTheme)
            .withActivityStyle(Libs.ActivityStyle.LIGHT_DARK_TOOLBAR)
            .withActivityTitle(getString(R.string.app_name))
            .withAboutAppName(getString(R.string.app_name))
            .withAboutDescription(getString(R.string.app_description))
            .withAboutIconShown(true)
            .withAboutVersionShown(true)
            .withListener(libsListener)
            .withAboutSpecial1("Play Store")
            .withAboutSpecial2("GitHub")
            .withLicenseShown(true)
            .supportFragment()
    }
}
