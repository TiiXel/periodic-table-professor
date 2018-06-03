package com.tiixel.periodictableprofessor.domain

import com.tiixel.periodictableprofessor.domain.card.CardRepository
import io.reactivex.Single
import java.util.Date
import javax.inject.Inject

class StatisticsInteractorImpl @Inject constructor(
    private val cardRepository: CardRepository
) {

    fun getKnownCardsCountPerDayHistory(): Single<Map<Date, Int>> {
        TODO()
    }

    fun getReviewCountPerDayHistory(): Single<Map<Date, Pair<Int, Int>>> {
        // review, cram
        // TODO: Create unit test based on static mock history
        return cardRepository.getReviewLog()
            .map { it.groupBy { it.element } }
            .map { it.map { it.key to it.value.sortedBy { it.reviewDate } }.toMap() }
            .map {
                it.flatMap { entry ->
                    entry.value.mapIndexed { i, r ->
                        Pair(
                            r,
                            if ((i + 1) < entry.value.size) {
                                r.isDueSoon(entry.value[i + 1].reviewDate)
                            } else {
                                r.isDueSoon(Date())
                            }
                        )
                    }
                }
            }
            .map { it.groupBy { Date(it.first.reviewDate.time - (it.first.reviewDate.time % (24 * 60 * 60 * 1000))) } }
            .map { it.mapValues { Pair(it.value.filter { it.second }.size, it.value.filter { !it.second }.size) } }
    }

    fun getPerformanceCounts(): Single<Map<Card.Companion.Performance, Int>> {
        TODO()
    }
}