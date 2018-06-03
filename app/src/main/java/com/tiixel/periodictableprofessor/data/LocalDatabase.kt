package com.tiixel.periodictableprofessor.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.tiixel.periodictableprofessor.data.note.local.db.UserNoteDao
import com.tiixel.periodictableprofessor.data.note.local.db.entity.UserNoteEntity
import com.tiixel.periodictableprofessor.data.review.local.db.ReviewDao
import com.tiixel.periodictableprofessor.data.review.local.db.entity.ReviewDataEntity

@Database(
    entities = arrayOf(
        ReviewDataEntity::class,
        UserNoteEntity::class
    ),
    version = 1
)
abstract class LocalDatabase : RoomDatabase() {

    abstract fun reviewDao(): ReviewDao

    abstract fun userNoteDao(): UserNoteDao
}