package com.tiixel.periodictableprofessor.dagger.module

import com.tiixel.periodictableprofessor.domain.card.interactor.CardInteractor
import com.tiixel.periodictableprofessor.domain.card.interactor.CardInteractorImpl
import com.tiixel.periodictableprofessor.domain.element.interactor.ElementInteractor
import com.tiixel.periodictableprofessor.domain.element.interactor.ElementInteractorImpl
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