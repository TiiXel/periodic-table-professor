package com.tiixel.periodictableprofessor.dagger.module

import com.tiixel.periodictableprofessor.util.schedulers.BaseSchedulerProvider
import com.tiixel.periodictableprofessor.util.schedulers.SchedulerProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ThreadModule {

    @Provides
    @Singleton
    fun provideSchedulerProvider(): BaseSchedulerProvider = SchedulerProvider
}