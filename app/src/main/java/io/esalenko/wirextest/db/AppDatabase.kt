package io.esalenko.wirextest.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import io.esalenko.wirextest.main.data.db.MarketDao
import io.esalenko.wirextest.main.data.model.MarketEntity

@Database(entities = [MarketEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun marketDao(): MarketDao

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