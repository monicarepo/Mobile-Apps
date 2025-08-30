package com.ms.countryapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ms.countryapp.data.Country

@Database(entities = [Country::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase(), DatabaseProvider {
    abstract override fun countryDao(): CountryDao


    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): DatabaseProvider? {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "country_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }


    }
}