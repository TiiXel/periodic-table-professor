package com.tiixel.periodictableprofessor.dagger.module

import android.app.Application
import android.arch.persistence.room.Room
import com.fstyle.library.helper.AssetSQLiteOpenHelperFactory
import com.tiixel.periodictableprofessor.data.LocalDatabase
import com.tiixel.periodictableprofessor.data.SqliteDatabase
import com.tiixel.periodictableprofessor.data.element.local.ElementSqliteDataSource
import com.tiixel.periodictableprofessor.data.mnemonic.local.MnemonicMixedDataSource
import com.tiixel.periodictableprofessor.data.note.local.db.UserNoteDbDataSource
import com.tiixel.periodictableprofessor.data.review.local.ReviewDbDataSource
import com.tiixel.periodictableprofessor.datasource.card.MnemonicLocalDataSource
import com.tiixel.periodictableprofessor.datasource.card.ReviewLocalDataSource
import com.tiixel.periodictableprofessor.datasource.card.UserNoteLocalDataSource
import com.tiixel.periodictableprofessor.datasource.element.ElementLocalDataSource
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataProvideModule {

    @Provides
    @Singleton
    fun provideAssetLocalDatabase(application: Application): SqliteDatabase {
        return Room.databaseBuilder(application.applicationContext, SqliteDatabase::class.java, "assets_database.db")
            .openHelperFactory(AssetSQLiteOpenHelperFactory())
            .build()
    }

    @Provides
    @Singleton
    fun provideLocalDatabase(application: Application): LocalDatabase {
        return Room.databaseBuilder(application.applicationContext, LocalDatabase::class.java, "user_database.db")
            .build()
    }
}

@Module
abstract class DataBindModule {

    @Binds
    @Singleton
    abstract fun bindElementLocalDataSource(elementAssetDbDataSource: ElementSqliteDataSource): ElementLocalDataSource

    @Binds
    @Singleton
    abstract fun bindMnemonicLocalDataSource(mnemonicMixedDataSource: MnemonicMixedDataSource): MnemonicLocalDataSource

    @Binds
    @Singleton
    abstract fun bindReviewLocalDataSource(reviewDbDataSource: ReviewDbDataSource): ReviewLocalDataSource

    @Binds
    @Singleton
    abstract fun bindUserNoteLocalDataSource(userNoteDbDataSource: UserNoteDbDataSource): UserNoteLocalDataSource
}