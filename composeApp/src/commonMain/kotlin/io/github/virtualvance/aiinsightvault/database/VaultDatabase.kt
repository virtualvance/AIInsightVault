package io.github.virtualvance.aiinsightvault.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor

// 1. Tell Room which tables to include and what the version is
@Database(entities = [InsightEntity::class], version = 1)
// 2. This links the database to the Multiplatform constructor
@ConstructedBy(VaultDatabaseConstructor::class)
abstract class VaultDatabase : RoomDatabase() {
    // 3. This provides the "Messenger" (DAO) to the rest of the app
    abstract fun vaultDao(): VaultDao
}

// 4. The "Expect" object tells KMP that each platform (Android/iOS)
// will provide its own specific way to build this database engine.
@Suppress("KotlinNoActualForExpect") // This tells the IDE: "Trust me, Room will generate this."
expect object VaultDatabaseConstructor : RoomDatabaseConstructor<VaultDatabase> {
    override fun initialize(): VaultDatabase
}
