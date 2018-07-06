package com.tiixel.periodictableprofessor.dagger.module

import android.app.Application
import android.arch.persistence.room.Room
import com.tiixel.periodictableprofessor.data.JsonAssets
import com.tiixel.periodictableprofessor.data.LocalDatabase
import com.tiixel.periodictableprofessor.data.element.local.ElementGsonDataSource
import com.tiixel.periodictableprofessor.data.mnemonic.local.MnemonicMixedDataSource
import com.tiixel.periodictableprofessor.data.note.local.db.UserNoteDbDataSource
import com.tiixel.periodictableprofessor.data.review.local.ReviewDbDataSource
import com.tiixel.periodictableprofessor.datarepository.card.contract.ReviewLocalDataSource
import com.tiixel.periodictableprofessor.datarepository.element.contract.ElementLocalDataSource
import com.tiixel.periodictableprofessor.datarepository.element.contract.MnemonicLocalDataSource
import com.tiixel.periodictableprofessor.datarepository.element.contract.UserNoteLocalDataSource
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataProvideModule {

    @Provides
    @Singleton
    fun provideLocalDatabase(application: Application): LocalDatabase {
        return Room.databaseBuilder(application.applicationContext, LocalDatabase::class.java, "user_database.db")
            .addMigrations(LocalDatabase.Companion.Migration_1_2())
            .build()
    }

    @Provides
    @Singleton
    fun provideJsonAssets(application: Application): JsonAssets {
        return JsonAssets(application)
    }
}

@Module
abstract class DataBindModule {

    @Binds
    @Singleton
    abstract fun bindElementLocalDataSource(elementGsonDataSource: ElementGsonDataSource): ElementLocalDataSource

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