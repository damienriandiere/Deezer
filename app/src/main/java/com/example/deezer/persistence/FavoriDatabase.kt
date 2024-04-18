package com.example.deezer.persistence

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Transaction
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.log

/**
 * Database class with a singleton Instance object.
 */
@Database(entities = [Favori::class], version = 1)
abstract class FavoriDatabase : RoomDatabase() {

    abstract fun favoriDao(): FavoriDao

    companion object {
        const val TAG: String = "TownDatabase"

        @Volatile
        private var INSTANCE: FavoriDatabase? = null

        fun getDatabase(
            context: Context
        ): FavoriDatabase {

            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                Log.d(TAG, "getDatabase: ")
                Room.databaseBuilder(context,
                    FavoriDatabase::class.java,
                    "favoris_database")
                    // Wipes and rebuilds instead of migrating if no Migration object.
                    // Migration is not part of this codelab.
                    //.fallbackToDestructiveMigration()
                    .build()
                    .also {
                        INSTANCE = it
                    }
            }
        }
    }
}
