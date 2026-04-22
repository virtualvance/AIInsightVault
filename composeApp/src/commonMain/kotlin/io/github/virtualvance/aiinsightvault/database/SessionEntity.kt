package io.github.virtualvance.aiinsightvault.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.time.Clock

@Entity(tableName = "sessions")
data class SessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val aiProvider: String, // Gemini, GPT, or Claude
    val createdAt: Long = Clock.System.now().toEpochMilliseconds()
)