package com.tiixel.periodictableprofessor.datarepository.element.contract

import com.tiixel.periodictableprofessor.datarepository.element.generic.GenericUserNote
import io.reactivex.Completable
import io.reactivex.Single

interface UserNoteLocalDataSource {

    fun getUserNotes(): Single<List<GenericUserNote>>

    fun updateUserNote(genericUserNote: GenericUserNote): Completable
}