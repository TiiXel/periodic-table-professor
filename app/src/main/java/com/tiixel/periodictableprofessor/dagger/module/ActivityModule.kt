package com.tiixel.periodictableprofessor.dagger.module

import com.tiixel.periodictableprofessor.ui.MainActivity
import com.tiixel.periodictableprofessor.ui.element.ElementActivity
import com.tiixel.periodictableprofessor.ui.elementlist.ElementTableFragment
import com.tiixel.periodictableprofessor.ui.study.StudyFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector
    internal abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector
    internal abstract fun contributeElementsTableFragment(): ElementTableFragment

    @ContributesAndroidInjector
    internal abstract fun contributeStudyFragment(): StudyFragment

    @ContributesAndroidInjector
    internal abstract fun contributeElementActivity(): ElementActivity
}