package com.tiixel.periodictableprofessor.data.note.local.db

import com.tiixel.periodictableprofessor.data.LocalDatabase
import com.tiixel.periodictableprofessor.datarepository.element.contract.UserNoteLocalDataSource
import com.tiixel.periodictableprofessor.datarepository.element.generic.GenericUserNote
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class UserNoteDbDataSource @Inject constructor(
    private val database: LocalDatabase
) : UserNoteLocalDataSource {

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