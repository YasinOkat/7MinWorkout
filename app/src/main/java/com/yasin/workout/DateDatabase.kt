package com.yasin.workout

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [DateEntity::class], version = 1)
abstract class DateDatabase:RoomDatabase() {

    abstract fun dateDao():DateDao

    companion object{

        @Volatile
        private var INSTANCE: DateDatabase? = null

        fun getInstance(context: Context):DateDatabase{

            synchronized(this){
                var instance = INSTANCE

                if(instance == null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        DateDatabase::class.java,
                        "date_database"
                    ).fallbackToDestructiveMigration()
                        .build()

                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}