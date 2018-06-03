package com.tiixel.periodictableprofessor.dagger

import android.app.Application
import com.tiixel.periodictableprofessor.dagger.module.ActivityModule
import com.tiixel.periodictableprofessor.dagger.module.DataProvideModule
import com.tiixel.periodictableprofessor.dagger.module.PresentationModule
import com.tiixel.periodictableprofessor.dagger.module.TestAppModule
import com.tiixel.periodictableprofessor.dagger.module.TestDataModule
import com.tiixel.periodictableprofessor.dagger.module.ThreadModule
import com.tiixel.periodictableprofessor.domain.element.ElementInteractor
import com.tiixel.periodictableprofessor.test.TestApp
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = arrayOf(
        ActivityModule::class,
        AndroidSupportInjectionModule::class,
        TestAppModule::class,
        TestDataModule::class,
        DataProvideModule::class,
        PresentationModule::class,
        ThreadModule::class
    )
)
interface TestAppComponent : AppComponent {

    fun elementRepository(): ElementInteractor

    fun inject(app: TestApp)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(app: Application): TestAppComponent.Builder

        fun build(): TestAppComponent
    }
}