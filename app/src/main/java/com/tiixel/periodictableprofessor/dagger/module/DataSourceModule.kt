package com.tiixel.periodictableprofessor.dagger.module

import com.tiixel.periodictableprofessor.datarepository.card.CardRepositoryImpl
import com.tiixel.periodictableprofessor.datarepository.element.ElementRepositoryImpl
import com.tiixel.periodictableprofessor.domain.card.CardRepository
import com.tiixel.periodictableprofessor.domain.element.ElementRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class DataSourceModule {

    @Binds
    @Singleton
    abstract fun bindElementRepository(elementRepositoryImpl: ElementRepositoryImpl): ElementRepository

    @Binds
    @Singleton
    abstract fun bindCardRepository(solidRepositoryImpl: CardRepositoryImpl): CardRepository
}