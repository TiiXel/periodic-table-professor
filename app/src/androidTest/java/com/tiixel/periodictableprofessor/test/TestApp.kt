package com.tiixel.periodictableprofessor.test

import android.app.Activity
import android.app.Application
import android.support.test.InstrumentationRegistry
import com.tiixel.periodictableprofessor.dagger.DaggerTestAppComponent
import com.tiixel.periodictableprofessor.dagger.TestAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class TestApp : Application(), HasActivityInjector {

    @Inject lateinit var activityInjector: DispatchingAndroidInjector<Activity>

    private lateinit var appComponent: TestAppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerTestAppComponent.builder().application(this).build()
        appComponent.inject(this)
    }

    companion object {

        fun appComponent(): TestAppComponent {
            return (InstrumentationRegistry.getTargetContext().applicationContext as TestApp).appComponent
        }
    }

    override fun activityInjector(): AndroidInjector<Activity> = activityInjector
}