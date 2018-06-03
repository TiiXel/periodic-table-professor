package com.tiixel.periodictableprofessor

import android.app.Activity
import android.app.Application
import com.tiixel.periodictableprofessor.dagger.DaggerAppComponent
import com.tspoon.traceur.Traceur
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class App : Application(), HasActivityInjector {

    @Inject lateinit var activityInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()
        DaggerAppComponent.builder().application(this).build().inject(this)
        Traceur.enableLogging();
    }

    override fun activityInjector(): AndroidInjector<Activity> = activityInjector
}