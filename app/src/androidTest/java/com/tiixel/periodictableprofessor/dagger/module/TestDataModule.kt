package com.tiixel.periodictableprofessor.dagger.module

import com.nhaarman.mockito_kotlin.mock
import com.tiixel.periodictableprofessor.domain.element.ElementInteractor
import com.tiixel.periodictableprofessor.domain.element.ElementRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class TestDataModule {

    @Provides
    @Singleton
    fun provideElementRepository(): ElementInteractor = mock()

    @Provides
    @Singleton
    fun provideElementLocalDataSource(): ElementRepository = mock()
}