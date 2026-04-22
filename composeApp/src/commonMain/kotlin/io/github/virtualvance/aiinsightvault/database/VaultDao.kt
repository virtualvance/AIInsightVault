package io.github.virtualvance.aiinsightvault.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface VaultDao {
    // --- Session Operations ---
    @Insert
    suspend fun insertSession(session: SessionEntity): Long

    @Query("SELECT * FROM sessions ORDER BY createdAt DESC")
    fun getAllSessions(): Flow<List<SessionEntity>>

    @Delete
    suspend fun deleteSession(session: SessionEntity)

    // --- Insight Operations ---
    @Insert
    suspend fun insertInsight(insight: InsightEntity)

    @Query("SELECT * FROM insights WHERE sessionId = :sessionId ORDER BY timestamp ASC")
    fun getInsightsForSession(sessionId: Long): Flow<List<InsightEntity>>
}