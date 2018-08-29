package com.tiixel.periodictableprofessor.presentation.home.statistics

import com.tiixel.periodictableprofessor.domain.review.interactor.ReviewInteractor
import com.tiixel.periodictableprofessor.util.schedulers.BaseSchedulerProvider
import io.reactivex.ObservableTransformer
import io.reactivex.rxkotlin.zipWith
import java.util.Calendar
import javax.inject.Inject

class StatisticsActionProcessor @Inject constructor(
    private val reviewInteractor: ReviewInteractor,
    private val schedulerProvider: BaseSchedulerProvider
) {

    private val loadStatsProcessor = ObservableTransformer<StatisticsAction, StatisticsResult> { actions ->
        actions.flatMap {
            reviewInteractor.countReviewablesNewPerPeriod(Calendar.DATE)
                .zipWith(reviewInteractor.countReviewsPerPeriod(Calendar.DATE))
                .zipWith(reviewInteractor.countKnownReviewablesPerPeriod(Calendar.DATE))
                .zipWith(reviewInteractor.countReviewsDuePerPeriod(Calendar.DATE))
                .toObservable()
                .map { StatisticsResult.LoadStatsResult.Success(it.first.first.first, it.first.first.second, it.first.second, it.second) }
                .cast(StatisticsResult.LoadStatsResult::class.java)
                .onErrorReturn { StatisticsResult.LoadStatsResult.Failure(it) }
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .startWith(StatisticsResult.LoadStatsResult.InFlight)
        }
    }

    internal val actionProcessor =
        ObservableTransformer<StatisticsAction, StatisticsResult> { action ->
            action.publish { shared ->
                shared.ofType(StatisticsAction.LoadStatsAction::class.java).compose(loadStatsProcessor)
            }
        }
}