package com.tiixel.periodictableprofessor.dagger.module

import com.tiixel.periodictableprofessor.domain.element.interactor.ElementInteractor
import com.tiixel.periodictableprofessor.domain.element.interactor.ElementInteractorImpl
import com.tiixel.periodictableprofessor.domain.review.interactor.ReviewInteractor
import com.tiixel.periodictableprofessor.domain.review.interactor.ReviewInteractorImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class DomainModule {

    @Binds
    @Singleton
    abstract fun bindElementInteractor(elementInteractorImpl: ElementInteractorImpl): ElementInteractor

    @Binds
    @Singleton
    abstract fun bindReviewInteractor(reviewInteractorImpl: ReviewInteractorImpl): ReviewInteractor
}