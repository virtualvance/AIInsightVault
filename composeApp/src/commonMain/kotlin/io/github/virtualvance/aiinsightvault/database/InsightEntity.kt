package io.github.virtualvance.aiinsightvault.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlin.time.Clock

@Entity(
    tableName = "insights",
    foreignKeys = [
        ForeignKey(
            entity = SessionEntity::class,
            parentColumns = ["id"],
            childColumns = ["sessionId"],
            onDelete = ForeignKey.CASCADE // If you delete a session, its insights are deleted too
        )
    ],
    indices = [Index(value = ["sessionId"])] // Makes searching for insights in a session faster
)
data class InsightEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val sessionId: Long, // This links the insight to the session
    val promptText: String,
    val responseText: String,
    val timestamp: Long = Clock.System.now().toEpochMilliseconds(),
    val isFavorite: Boolean = false
)