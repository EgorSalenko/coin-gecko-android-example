package io.esalenko.coingecko.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import io.esalenko.coingecko.main.data.db.MarketDao
import io.esalenko.coingecko.main.data.model.MarketEntity

@Database(entities = [MarketEntity::class, RemoteKeysEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun marketDao(): MarketDao

    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }

    }
}