package com.tiixel.periodictableprofessor.dagger.module

import com.nhaarman.mockito_kotlin.mock
import com.tiixel.periodictableprofessor.domain.card.contract.CardRepository
import com.tiixel.periodictableprofessor.domain.element.contract.ElementRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class TestDataRepositoryModule {

    @Provides
    @Singleton
    fun provideElementRepository(): ElementRepository = mock()

    @Provides
    @Singleton
    fun provideReviewRepository(): CardRepository = mock()
}