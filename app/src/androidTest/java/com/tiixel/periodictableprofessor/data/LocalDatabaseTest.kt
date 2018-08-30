package com.tiixel.periodictableprofessor.data

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.db.framework.FrameworkSQLiteOpenHelperFactory
import android.arch.persistence.room.Room
import android.arch.persistence.room.testing.MigrationTestHelper
import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.tiixel.periodictableprofessor.data.review.local.db.REVIEW_LOG_TABLE_NAME
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LocalDatabaseTest {

    private val databaseName = "test-db"

    @Rule @JvmField val testHelper: MigrationTestHelper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        LocalDatabase::class.java.canonicalName,
        FrameworkSQLiteOpenHelperFactory()
    )

    @Test
    fun migration_1_2_containsCorrectData() {
        val db = testHelper.createDatabase(databaseName, 1)
        db.insertReview_1(1, 0.3f, 10, 20, 0)
        db.close()

        testHelper.runMigrationsAndValidate(databaseName, 2, true, LocalDatabase.Companion.Migration_1_2())

        val migratedReview = getMigratedRoomDatabase().reviewDao().getReviewLog().first()
        assertEquals(1.toByte(), migratedReview.itemId)
        assertEquals(null, migratedReview.face)
        assertEquals(10, migratedReview.review_date)
        assertEquals(30, migratedReview.next_due_date)
        assertEquals(0, migratedReview.performance)
        assertEquals(0.3f, migratedReview.aggregated_item_difficulty)
    }

    private fun SupportSQLiteDatabase.insertReview_1(
        element: Byte,
        difficulty: Float,
        review_date: Long,
        next_interval: Long,
        performance: Int
    ) {
        val value = ContentValues()
        value.put("element", element)
        value.put("difficulty", difficulty)
        value.put("review_date", review_date)
        value.put("next_interval", next_interval)
        value.put("performance", performance)

        insert(REVIEW_LOG_TABLE_NAME, SQLiteDatabase.CONFLICT_REPLACE, value)
    }

    private fun getMigratedRoomDatabase(): LocalDatabase {
        val database = Room.databaseBuilder(
            InstrumentationRegistry.getTargetContext(),
            LocalDatabase::class.java,
            databaseName
        )
            .addMigrations(LocalDatabase.Companion.Migration_1_2())
            .build()
        testHelper.closeWhenFinished(database)
        return database
    }
}