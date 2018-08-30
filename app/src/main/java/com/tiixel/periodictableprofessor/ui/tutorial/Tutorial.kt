package com.tiixel.periodictableprofessor.ui.tutorial

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import com.hololo.tutorial.library.Step
import com.hololo.tutorial.library.TutorialActivity
import com.tiixel.periodictableprofessor.R
import com.tiixel.periodictableprofessor.ui.MainActivity

class Tutorial : TutorialActivity() {

    companion object {
        val tutorialSharedPreferencesFile = "com.tiixel.periodictableprofessor.tutorial"
        val tutorialCompletedSharedPreferencesKey = "com.tiixel.periodictableprofessor.tutorial.completed"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addFragment(
            Step.Builder()
                .setTitle(getString(R.string.tutorial_welcome_title))
                .setContent(getString(R.string.tutorial_welcome_content))
                .setBackgroundColor(ResourcesCompat.getColor(resources, R.color.colorPrimaryDark, baseContext.theme))
                .build()
        )
        addFragment(
            Step.Builder()
                .setTitle(getString(R.string.tutorial_cards_title))
                .setContent(getString(R.string.tutorial_cards_content))
                .setDrawable(R.drawable.tutorial_card)
                .setBackgroundColor(ResourcesCompat.getColor(resources, R.color.colorPrimaryDark, baseContext.theme))
                .build()
        )
        addFragment(
            Step.Builder()
                .setTitle(getString(R.string.tutorial_reviewing_title))
                .setContent(getString(R.string.tutorial_reviewing_content))
                .setDrawable(R.drawable.tutorial_reviewing_picture)
                .setBackgroundColor(ResourcesCompat.getColor(resources, R.color.colorPrimaryDark, baseContext.theme))
                .build()
        )
        addFragment(
            Step.Builder()
                .setTitle(getString(R.string.tutorial_intervals_title))
                .setContent(getString(R.string.tutorial_intervals_content))
                .setDrawable(R.drawable.tutorial_reviewing_all)
                .setBackgroundColor(ResourcesCompat.getColor(resources, R.color.colorPrimaryDark, baseContext.theme))
                .build()
        )
    }

    override fun finishTutorial() {
        val sharedPref = getSharedPreferences(tutorialSharedPreferencesFile, Context.MODE_PRIVATE)
        with (sharedPref.edit()) {
            putBoolean(tutorialCompletedSharedPreferencesKey, true)
            commit()
        }
        startActivity(Intent(this, MainActivity::class.java))
    }
}