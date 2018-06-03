package com.tiixel.periodictableprofessor.dagger.module

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.tiixel.periodictableprofessor.dagger.ViewModelFactory
import com.tiixel.periodictableprofessor.presentation.elementlist.ElementListViewModel
import com.tiixel.periodictableprofessor.presentation.review.ReviewViewModel
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
    @ViewModelKey(ReviewViewModel::class)
    abstract fun bindLearnViewModel(viewModel: ReviewViewModel): ViewModel

    @Binds
    @Singleton
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}