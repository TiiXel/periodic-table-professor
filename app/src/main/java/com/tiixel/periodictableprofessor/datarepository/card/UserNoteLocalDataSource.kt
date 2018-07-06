package com.tiixel.periodictableprofessor.datarepository.card

import com.tiixel.periodictableprofessor.datarepository.card.generic.GenericUserNote
import io.reactivex.Completable
import io.reactivex.Single

interface UserNoteLocalDataSource {

    fun getUserNote(element: Byte): Single<GenericUserNote>

    fun getUserNotes(): Single<List<GenericUserNote>>

    fun updateUserNote(genericUserNote: GenericUserNote): Completable
}