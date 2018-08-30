package com.tiixel.periodictableprofessor.dagger.module

import com.tiixel.periodictableprofessor.datarepository.element.ElementRepositoryImpl
import com.tiixel.periodictableprofessor.datarepository.review.ReviewRepositoryImpl
import com.tiixel.periodictableprofessor.domain.element.contract.ElementRepository
import com.tiixel.periodictableprofessor.domain.review.contract.ReviewRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class DataRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindElementRepository(elementRepositoryImpl: ElementRepositoryImpl): ElementRepository

    @Binds
    @Singleton
    abstract fun bindReviewRepository(reviewRepositoryImpl: ReviewRepositoryImpl): ReviewRepository
}