package com.tiixel.periodictableprofessor.datarepository.card

import com.tiixel.periodictableprofessor.datarepository.card.generic.StoredUserNote
import io.reactivex.Completable
import io.reactivex.Single

interface UserNoteLocalDataSource {

    fun getUserNote(element: Byte): Single<StoredUserNote>

    fun getUserNotes(): Single<List<StoredUserNote>>

    fun updateUserNote(storedUserNote: StoredUserNote): Completable
}