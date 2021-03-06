package com.tiixel.periodictableprofessor.dagger.module

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.tiixel.periodictableprofessor.dagger.ViewModelFactory
import com.tiixel.periodictableprofessor.presentation.element.ElementViewModel
import com.tiixel.periodictableprofessor.presentation.elementlist.ElementListViewModel
import com.tiixel.periodictableprofessor.presentation.home.statistics.StatisticsViewModel
import com.tiixel.periodictableprofessor.presentation.study.StudyViewModel
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Singleton
import kotlin.reflect.KClass

@MustBeDocumented
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class ViewModelKey(val value: KClass<out ViewModel>)

@Module
internal abstract class PresentationModule {

    @Binds
    @IntoMap
    @ViewModelKey(ElementListViewModel::class)
    abstract fun bindElementsListViewModel(viewModel: ElementListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(StudyViewModel::class)
    abstract fun bindStudyViewModel(viewModel: StudyViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ElementViewModel::class)
    abstract fun bindElementViewModel(viewModel: ElementViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(StatisticsViewModel::class)
    abstract fun bindStatisticsViewModel(viewModel: StatisticsViewModel): ViewModel

    @Binds
    @Singleton
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}