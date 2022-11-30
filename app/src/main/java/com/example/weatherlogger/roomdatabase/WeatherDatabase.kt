package com.example.weatherlogger.roomdatabase


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase


@Database(entities = [Weather::class], exportSchema = false, version = 1)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao?

    companion object {
        private const val DB_NAME = "weather_db"
        private var instance: WeatherDatabase? = null
        @Synchronized
        fun getInstance(context: Context): WeatherDatabase? {
            if (instance == null) {

                instance = Room.databaseBuilder(context.applicationContext, WeatherDatabase::class.java,
                    DB_NAME)
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build()

//                instance = Room.databaseBuilder(
//                    context.applicationContext,
//                    WeatherDatabase::class.java,
//                    DB_NAME
//                )
////                    .fallbackToDestructiveMigration()
//                    .build()
            }


            return instance
        }

        private val roomCallback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
//                populateDatabase(instance!!)
            }
        }
    }



}