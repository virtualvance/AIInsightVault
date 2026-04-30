package io.github.virtualvance.aiinsightvault.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.random.Random

@Entity(tableName = "insights")
data class InsightEntity(
    // Generating a random number as our Agnostic ID for the demo
    @PrimaryKey
    val id: String = "insight_${Random.nextLong(1000000, 9999999)}",
    val title: String,
    val source: String = "Gemini",
    val rawContent: String,
    val summary: String? = null,
    val tags: String? = null,
    val timestamp: Long = 0L, // Hardcoded to 0 for the demo; we'll fix time later
    val isFavorite: Boolean = false
)