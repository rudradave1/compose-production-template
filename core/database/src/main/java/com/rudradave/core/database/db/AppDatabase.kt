package com.rudradave.core.database.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rudradave.core.database.dao.PostDao
import com.rudradave.core.database.entity.PostEntity

/**
 * Main Room database for cached app data.
 */
@Database(
    entities = [PostEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    /**
     * Returns post DAO.
     */
    abstract fun postDao(): PostDao
}
