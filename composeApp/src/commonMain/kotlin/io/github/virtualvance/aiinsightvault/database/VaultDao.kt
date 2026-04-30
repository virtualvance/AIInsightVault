package io.github.virtualvance.aiinsightvault.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface VaultDao {
    // --- Session Operations (Preserved) ---
    @Insert
    suspend fun insertSession(session: SessionEntity): Long

    @Query("SELECT * FROM sessions ORDER BY createdAt DESC")
    fun getAllSessions(): Flow<List<SessionEntity>>

    @Delete
    suspend fun deleteSession(session: SessionEntity)

    // --- NEW Sprint 4 Agnostic Insight Operations ---

    // Upsert logic: Replaces the old entry if the same ID is saved
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInsight(insight: InsightEntity)

    // Gets all insights globally, since they are no longer tied to a specific session ID
    @Query("SELECT * FROM insights ORDER BY timestamp DESC")
    fun getAllInsights(): Flow<List<InsightEntity>>

    // Allows the app to check if an insight with this title already exists
    @Query("SELECT * FROM insights WHERE title = :title LIMIT 1")
    suspend fun getInsightByTitle(title: String): InsightEntity?
}