package com.tiixel.periodictableprofessor.data

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.migration.Migration
import com.tiixel.periodictableprofessor.data.note.local.db.UserNoteDao
import com.tiixel.periodictableprofessor.data.note.local.db.entity.UserNoteEntity
import com.tiixel.periodictableprofessor.data.review.local.db.REVIEW_LOG_TABLE_NAME
import com.tiixel.periodictableprofessor.data.review.local.db.ReviewDao
import com.tiixel.periodictableprofessor.data.review.local.db.entity.ReviewEntity

@Database(
    entities = arrayOf(
        ReviewEntity::class,
        UserNoteEntity::class
    ),
    version = 2
)
abstract class LocalDatabase : RoomDatabase() {

    abstract fun reviewDao(): ReviewDao

    abstract fun userNoteDao(): UserNoteDao

    companion object {

        class Migration_1_2 : Migration(1, 2) {

            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE $REVIEW_LOG_TABLE_NAME RENAME TO tmp_$REVIEW_LOG_TABLE_NAME")
                database.execSQL(
                    "CREATE TABLE $REVIEW_LOG_TABLE_NAME( " +
                        "  itemId INTEGER NOT NULL, " +
                        "  face INTEGER, " +
                        "  review_date INTEGER NOT NULL, " +
                        "  next_due_date INTEGER NOT NULL, " +
                        "  performance INTEGER NOT NULL, " +
                        "  aggregated_item_difficulty REAL NOT NULL, " +
                        "  PRIMARY KEY(review_date) " +
                        ")"
                )
                database.execSQL(
                    "INSERT INTO $REVIEW_LOG_TABLE_NAME(itemId, face, review_date, next_due_date, performance, aggregated_item_difficulty)" +
                        "SELECT element, NULL, review_date, review_date + next_interval, performance, difficulty FROM tmp_$REVIEW_LOG_TABLE_NAME"
                )
                database.execSQL("DROP TABLE tmp_$REVIEW_LOG_TABLE_NAME")
            }
        }
    }
}