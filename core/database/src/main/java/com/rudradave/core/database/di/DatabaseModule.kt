package com.rudradave.core.database.di

import android.content.Context
import androidx.room.Room
import com.rudradave.core.database.dao.PostDao
import com.rudradave.core.database.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Provides Room database dependencies.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    /**
     * Provides app database singleton.
     */
    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "compose_production_template.db"
        ).build()
    }

    /**
     * Provides post DAO.
     */
    @Provides
    fun providePostDao(
        appDatabase: AppDatabase
    ): PostDao = appDatabase.postDao()
}
