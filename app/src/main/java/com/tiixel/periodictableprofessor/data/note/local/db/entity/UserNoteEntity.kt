package com.tiixel.periodictableprofessor.data.note.local.db.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.tiixel.periodictableprofessor.data.note.local.db.USER_NOTE_TABLE_NAME

@Entity(tableName = USER_NOTE_TABLE_NAME)
data class UserNoteEntity(
    @PrimaryKey
    val element: Byte,
    val user_note: String?
)
