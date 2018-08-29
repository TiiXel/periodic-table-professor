package com.tiixel.periodictableprofessor.presentation.home.statistics

import android.arch.lifecycle.ViewModel
import com.tiixel.periodictableprofessor.presentation.base.MviViewModel
import com.tiixel.periodictableprofessor.util.extensions.notOfType
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class StatisticsViewModel @Inject constructor(
    private val actionProcessor: StatisticsActionProcessor
) : ViewModel(), MviViewModel<StatisticsIntent, StatisticsViewState> {

    private val intentsSubjects: PublishSubject<StatisticsIntent> = PublishSubject.create()
    private val statesObservables: Observable<StatisticsViewState> = compose()

    private val intentFilter: ObservableTransformer<StatisticsIntent, StatisticsIntent>
        get() = ObservableTransformer { intents ->
            intents.publish { shared ->
                Observable.merge(
                    shared.ofType(StatisticsIntent.InitialIntent::class.java).take(1),
                    shared.notOfType(StatisticsIntent.InitialIntent::class.java)
                )
            }
        }

    override fun processIntents(intents: Observable<StatisticsIntent>) {
        intents.subscribe(intentsSubjects)
    }

    override fun states(): Observable<StatisticsViewState> = statesObservables

    private fun compose(): Observable<StatisticsViewState> {
        return intentsSubjects
            .compose(intentFilter)
            .map(this::actionFromIntent)
            .compose(actionProcessor.actionProcessor)
            .scan(StatisticsViewState.idle(), reducer)
            .distinctUntilChanged()
            .replay(1)
            .autoConnect(0)
    }

    private fun actionFromIntent(intent: StatisticsIntent): StatisticsAction {
        return when (intent) {
            is StatisticsIntent.InitialIntent -> StatisticsAction.LoadStatsAction(null)
        }
    }

    companion object {

        private val reducer = BiFunction { previousState: StatisticsViewState, result: StatisticsResult ->
            when (result) {
                is StatisticsResult.LoadStatsResult.Success -> {
                    previousState.copy(
                        loadingInProgress = false,
                        dataItemsNewPerDay = result.itemsNewPerDay,
                        dataReviewsPerDay = result.reviewsPerDay,
                        dataKnownReviewablesPerDay = result.knownReviewablesPerDay,
                        dataReviewsDuePerDay = result.reviewsDuePerPeriod
                    )
                }
                is StatisticsResult.LoadStatsResult.Failure -> {
                    previousState.copy(
                        loadingInProgress = false,
                        loadingFailedCause = result.error,
                        dataItemsNewPerDay = null,
                        dataReviewsPerDay = null,
                        dataKnownReviewablesPerDay = null,
                        dataReviewsDuePerDay = null
                    )
                    throw result.error
                }
                is StatisticsResult.LoadStatsResult.InFlight -> {
                    previousState.copy(
                        loadingInProgress = true
                    )
                }
            }
        }
    }
}