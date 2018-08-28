package com.tiixel.periodictableprofessor.ui.base

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v7.app.AppCompatActivity
import com.mikepenz.aboutlibraries.Libs
import com.mikepenz.aboutlibraries.LibsBuilder
import com.mikepenz.google_material_typeface_library.GoogleMaterial
import com.mikepenz.iconics.IconicsDrawable
import com.tiixel.periodictableprofessor.R
import com.tiixel.periodictableprofessor.ui.elementlist.ElementTableFragment
import com.tiixel.periodictableprofessor.ui.study.StudyFragment
import kotlinx.android.synthetic.main.main_layout_drawer.*

abstract class MainDrawerActivity(val contentLayout: Int) : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.main_layout_drawer)

        setSupportActionBar(toolbar)
        layoutInflater.inflate(contentLayout, content_frame, true)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(
                IconicsDrawable(this@MainDrawerActivity)
                    .icon(GoogleMaterial.Icon.gmd_menu)
                    .color(Color.WHITE)
                    .sizeDp(20)
            )
        }

        main_layout_drawer.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_study -> {
                    val intent = Intent(this, StudyFragment::class.java)
                    startActivity(intent)
                }
                R.id.nav_periodic_table -> {
                    val intent = Intent(this, ElementTableFragment::class.java)
                    startActivity(intent)
                }
                R.id.nav_about -> {
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
            menuItem.isChecked = true
            drawer_layout.closeDrawers()
            true
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        drawer_layout.openDrawer(GravityCompat.START)
        return true
    }
}