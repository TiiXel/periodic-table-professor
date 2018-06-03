package com.tiixel.periodictableprofessor.dagger.module

import com.tiixel.periodictableprofessor.ui.elementlist.ElementTableActivity
import com.tiixel.periodictableprofessor.ui.home.HomeActivity
import com.tiixel.periodictableprofessor.ui.review.ReviewActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector
    internal abstract fun contributeElementsTableActivity(): ElementTableActivity

    @ContributesAndroidInjector
    internal abstract fun contributeHomeActivity(): HomeActivity

    @ContributesAndroidInjector
    internal abstract fun contributeLearnActivity(): ReviewActivity

}