package com.tiixel.periodictableprofessor.dagger

import android.app.Application
import com.tiixel.periodictableprofessor.App
import com.tiixel.periodictableprofessor.dagger.module.ActivityModule
import com.tiixel.periodictableprofessor.dagger.module.AppModule
import com.tiixel.periodictableprofessor.dagger.module.DataBindModule
import com.tiixel.periodictableprofessor.dagger.module.DataProvideModule
import com.tiixel.periodictableprofessor.dagger.module.DataRepositoryModule
import com.tiixel.periodictableprofessor.dagger.module.DomainModule
import com.tiixel.periodictableprofessor.dagger.module.PresentationModule
import com.tiixel.periodictableprofessor.dagger.module.ThreadModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = arrayOf(
        DataProvideModule::class,
        DataBindModule::class,
        DataRepositoryModule::class,
        DomainModule::class,
        PresentationModule::class,
        ActivityModule::class,
        ThreadModule::class,
        AndroidSupportInjectionModule::class,
        AppModule::class
    )
)
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(app: Application): Builder

        fun build(): AppComponent
    }

    fun inject(app: App)
}