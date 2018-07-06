package com.tiixel.periodictableprofessor.datarepository.element

import com.tiixel.periodictableprofessor.datarepository.card.MnemonicLocalDataSource
import com.tiixel.periodictableprofessor.datarepository.card.UserNoteLocalDataSource
import com.tiixel.periodictableprofessor.datarepository.element.generic.GenericElement
import com.tiixel.periodictableprofessor.datarepository.element.mapper.ElementMapper
import com.tiixel.periodictableprofessor.domain.Element
import com.tiixel.periodictableprofessor.domain.element.ElementRepository
import io.reactivex.Single
import io.reactivex.rxkotlin.zipWith
import javax.inject.Inject

class ElementRepositoryImpl @Inject constructor(
    private val elementLocalDataSource: ElementLocalDataSource,
    private val mnemonicLocalDataSource: MnemonicLocalDataSource,
    private val userNoteLocalDataSource: UserNoteLocalDataSource
) : ElementRepository {

    private var cachedElements: MutableMap<Byte, GenericElement> = emptyMap<Byte, GenericElement>().toMutableMap()

    override fun getElements(): Single<List<Element>> {
        return if (cachedElements.isNotEmpty()) {
            Single.just(cachedElements.values.toList())
        } else {
            elementLocalDataSource.getElements()
        }
            .doOnSuccess {
                cachedElements = it.map { it.atomicNumber to it }.toMap().toMutableMap()
            }
            .zipWith(mnemonicLocalDataSource.getMnemonics()) { elements, mnemonics ->
                elements.map { element ->
                    element to mnemonics.firstOrNull { it.element == element.atomicNumber }
                }
            }
            .zipWith(userNoteLocalDataSource.getUserNotes()) { elements, userNote ->
                elements.map { element ->
                    element to userNote.firstOrNull { it.element == element.first.atomicNumber }
                }
            }
            .map {
                it.map { ElementMapper.toDomain(it.first.first, it.first.second, it.second) }
            }
    }
}