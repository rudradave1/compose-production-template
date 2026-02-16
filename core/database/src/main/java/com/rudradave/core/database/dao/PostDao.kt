package com.rudradave.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rudradave.core.database.entity.PostEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO for post persistence operations.
 */
@Dao
interface PostDao {

    /**
     * Streams all posts ordered by id.
     */
    @Query("SELECT * FROM posts ORDER BY id ASC")
    fun observePosts(): Flow<List<PostEntity>>

    /**
     * Inserts or replaces all posts.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(posts: List<PostEntity>)

    /**
     * Deletes all cached posts.
     */
    @Query("DELETE FROM posts")
    suspend fun clearAll()
}
