package com.tiixel.periodictableprofessor.data.note.local.db

import com.tiixel.periodictableprofessor.data.LocalDatabase
import com.tiixel.periodictableprofessor.datarepository.card.UserNoteLocalDataSource
import com.tiixel.periodictableprofessor.datarepository.card.generic.StoredUserNote
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class UserNoteDbDataSource @Inject constructor(
    private val database: LocalDatabase
) : UserNoteLocalDataSource {

    override fun getUserNote(element: Byte): Single<StoredUserNote> {
        return Single.defer {
            val userNote = database.userNoteDao().getUserNote(element)
            Single.just(StoredUserNote(element = element, userNote = userNote?.user_note))
        }
    }

    override fun getUserNotes(): Single<List<StoredUserNote>> {
        return Single.defer {
            val userNotes = database.userNoteDao().getUserNotes()
            Single.just(userNotes.mapNotNull { StoredUserNote(element = it!!.element, userNote = it!!.user_note) })
        }
    }

    override fun updateUserNote(storedUserNote: StoredUserNote): Completable {
        TODO()
    }
}