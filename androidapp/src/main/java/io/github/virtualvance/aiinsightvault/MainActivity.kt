package io.github.virtualvance.aiinsightvault

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import io.github.virtualvance.aiinsightvault.database.VaultDatabase
import io.github.virtualvance.aiinsightvault.database.VaultDatabaseConstructor
import io.github.virtualvance.aiinsightvault.database.InsightEntity
import io.github.virtualvance.aiinsightvault.database.VaultDao
import kotlinx.coroutines.flow.Flow

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            val db = remember {
                val dbBuilder = androidx.room.Room.databaseBuilder<VaultDatabase>(
                    context = applicationContext,
                    name = applicationContext.getDatabasePath("vault.db").absolutePath,
                    factory = { VaultDatabaseConstructor.initialize() }
                )
                dbBuilder.setDriver(BundledSQLiteDriver())
                    .setQueryCoroutineContext(kotlinx.coroutines.Dispatchers.IO)
                    .fallbackToDestructiveMigration(dropAllTables = true)
                    .build()
            }
            
            App(vaultDao = db.vaultDao())
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    val mockDao = object : VaultDao {
        override suspend fun insertInsight(insight: InsightEntity) {}
        override fun getAllInsights(): Flow<List<InsightEntity>> = kotlinx.coroutines.flow.flowOf(emptyList())
        override suspend fun getInsightByTitle(title: String): InsightEntity? = null
    }
    App(vaultDao = mockDao)
}
