package com.tiixel.periodictableprofessor.datarepository.element

import com.tiixel.periodictableprofessor.datarepository.element.generic.GenericUserNote
import io.reactivex.Completable
import io.reactivex.Single

interface UserNoteLocalDataSource {

    fun getUserNote(element: Byte): Single<GenericUserNote>

    fun getUserNotes(): Single<List<GenericUserNote>>

    fun updateUserNote(genericUserNote: GenericUserNote): Completable
}