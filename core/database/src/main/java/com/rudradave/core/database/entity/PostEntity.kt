package com.rudradave.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity for post records cached locally.
 */
@Entity(tableName = "posts")
data class PostEntity(
    @PrimaryKey val id: Long,
    val userId: Long,
    val title: String,
    val body: String,
    val syncedAtEpochMillis: Long
)
