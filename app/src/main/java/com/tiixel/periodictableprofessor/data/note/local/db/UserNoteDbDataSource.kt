package com.tiixel.periodictableprofessor.data.note.local.db

import com.tiixel.periodictableprofessor.data.LocalDatabase
import com.tiixel.periodictableprofessor.datarepository.element.UserNoteLocalDataSource
import com.tiixel.periodictableprofessor.datarepository.element.generic.GenericUserNote
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class UserNoteDbDataSource @Inject constructor(
    private val database: LocalDatabase
) : UserNoteLocalDataSource {

    override fun getUserNote(element: Byte): Single<GenericUserNote> {
        return Single.defer {
            val userNote = database.userNoteDao().getUserNote(element)
            Single.just(
                GenericUserNote(
                    element = element,
                    userNote = userNote?.user_note
                )
            )
        }
    }

    override fun getUserNotes(): Single<List<GenericUserNote>> {
        return Single.defer {
            val userNotes = database.userNoteDao().getUserNotes()
            Single.just(userNotes.mapNotNull {
                GenericUserNote(
                    element = it!!.element,
                    userNote = it!!.user_note
                )
            })
        }
    }

    override fun updateUserNote(genericUserNote: GenericUserNote): Completable {
        TODO()
    }
}