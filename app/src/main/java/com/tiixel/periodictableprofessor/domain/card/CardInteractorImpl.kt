package com.tiixel.periodictableprofessor.domain.card

import com.tiixel.periodictableprofessor.domain.Card
import com.tiixel.periodictableprofessor.domain.ReviewData
import com.tiixel.periodictableprofessor.domain.algorithm.Sm2Plus
import com.tiixel.periodictableprofessor.domain.element.ElementRepository
import com.tiixel.periodictableprofessor.domain.exception.AllCardsAreNewException
import com.tiixel.periodictableprofessor.domain.exception.NoCardsAreNewException
import com.tiixel.periodictableprofessor.domain.exception.NoCardsDueSoonException
import com.tiixel.periodictableprofessor.domain.exception.NoMnemonicForThisElementException
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.rxkotlin.zipWith
import java.util.Date
import javax.inject.Inject

class CardInteractorImpl @Inject constructor(
    private val cardRepository: CardRepository,
    private val elementRepository: ElementRepository
) : CardInteractor {

    private var lastReviews: MutableMap<Byte, ReviewData>? = null

    private fun getLastReviews(): Single<Map<Byte, ReviewData>> {
        return if (lastReviews == null) {
            cardRepository.getReviewLog()
                .map { logs -> logs.groupBy { it.element } }
                .map { logs -> logs.map { it.key to it.value.sortedBy { it.reviewDate }.last() }.toMap() }
                .doOnSuccess { logs -> lastReviews = logs.toMutableMap() }
        } else {
            Single.just(lastReviews)
        }
    }

    override fun getNewCardForReview(): Single<Pair<Card, Card.Companion.Face>> {
        // TODO: Check that logs is shorter than mnemonic list
        return elementRepository.getElements()
            // Get all atomic numbers
            .map { it.map { it.atomicNumber } }
            // Keep atomic numbers NOT present in logs (ie never seen ones)
            .zipWith(getLastReviews(), { elements, logs ->
                elements.map { element ->
                    if (element !in logs.keys) return@map element else return@map null
                }.filterNotNull()
            })
            // Return error if no cards are never seen
            .filter { it.isNotEmpty() }
            .switchIfEmpty(Single.error(NoCardsAreNewException))
            // Select one random atomic number among never seen ones
            .map { it.toList().shuffled().first() }
            // Get the card
            .flatMap { cardRepository.getCard(it) }
            // Retry when mnemonic is null
            .filter { it.mnemonic != null }
            .switchIfEmpty(Single.error(NoMnemonicForThisElementException))
            .retryWhen {
                it.flatMap<Boolean> {
                    if (it === NoMnemonicForThisElementException)
                        Flowable.just(true)
                    else
                        Flowable.error(it)
                }
            }
            // Select face
            .map { Pair(it, Card.Companion.Face.PICTURE) }
    }

    override fun getNextCardForReview(dueSoonOnly: Boolean): Single<Pair<Card, Card.Companion.Face>> {
        return getLastReviews()
            .filter { it.isNotEmpty() }
            .switchIfEmpty(Single.error(AllCardsAreNewException))
            .map { it.values }
            .map {
                if (dueSoonOnly) {
                    it.filter { it.isDueSoon(reference = Date()) }
                } else {
                    it
                }
            }
            .filter { it.isNotEmpty() }
            .switchIfEmpty(Single.error(NoCardsDueSoonException))
            .map { it.sortedBy { it.nextDateOverdue }.last() }
            .flatMap { cardRepository.getCard(it.element) }
            .map { Pair(it, Card.Companion.Face.SYMBOL) }
    }

    override fun getNextReviewDate(): Maybe<Date> {
        return getLastReviews()
            .filter { it.isNotEmpty() }
            .flatMap {
                val next = it.values.sortedBy { it.nextDateOverdue }.lastOrNull { !it.isDueSoon(Date()) }
                if (next == null) Maybe.empty()
                else Maybe.just(next)
            }
            .map { it.nextDate }
    }

    override fun countCardsDueToday(reference: Date): Single<Int> {
        return getLastReviews()
            .map { it.filter { it.value.isDueOnDay(reference) }.size }
    }

    override fun countCardsDueSoon(reference: Date): Single<Int> {
        return getLastReviews()
            .map { it.filter { it.value.isDueSoon(reference) }.size }
    }

    override fun countCardsNewOnDay(day: Date): Single<Int> {
        return cardRepository.getReviewLog()
            .map { logs -> logs.groupBy { it.element } }
            .map { logs -> logs.map { it.key to it.value.sortedBy { it.reviewDate }.first() }.map { it.second } }
            .map { logs -> logs.filter { it.isDueOnDay(day) } }
            .map { logs -> logs.size }
    }

    override fun reviewCard(element: Byte, performance: Card.Companion.Performance, time: Date): Completable {
        return getLastReviews()
            .flatMapMaybe {
                if (it.containsKey(element))
                    Maybe.just(it[element])
                else
                    Maybe.empty()
            }
            .map { Sm2Plus.compute(it, performance, time) }
            // TODO: The new entry in logs should be added when first retrieving the card
            .defaultIfEmpty(Sm2Plus.defaultReview(element, time))
            .doOnSuccess { lastReviews?.put(it.element, it) }
            .flatMapCompletable { cardRepository.logReview(it) }
    }

    override fun editCard(card: Card): Completable {
        TODO()
    }
}