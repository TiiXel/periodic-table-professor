package com.tiixel.periodictableprofessor.data.note.local.db

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import com.tiixel.periodictableprofessor.data.note.local.db.entity.UserNoteEntity

const val USER_NOTE_TABLE_NAME = "user_note"

@Dao
abstract class UserNoteDao {

    @Query(value = "SELECT * FROM user_note WHERE element = :element")
    abstract fun getUserNote(element: Byte): UserNoteEntity?

    @Query(value = "SELECT * FROM user_note")
    abstract fun getUserNotes(): List<UserNoteEntity?>
}