package com.tiixel.periodictableprofessor.dagger.module

import com.tiixel.periodictableprofessor.domain.card.CardInteractor
import com.tiixel.periodictableprofessor.domain.card.CardInteractorImpl
import com.tiixel.periodictableprofessor.domain.element.ElementInteractor
import com.tiixel.periodictableprofessor.domain.element.ElementInteractorImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class DomainModule {

    @Binds
    @Singleton
    abstract fun bindElementUseCase(elementUseCaseImpl: ElementInteractorImpl): ElementInteractor

    @Binds
    @Singleton
    abstract fun bindCardUseCase(cardUseCaseImpl: CardInteractorImpl): CardInteractor
}