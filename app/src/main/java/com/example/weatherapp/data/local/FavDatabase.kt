package com.example.weatherapp.data.local

import android.app.Application
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weatherapp.data.model.ReminderModel

@Database(entities = [FavModel::class, ReminderModel::class], version = 1, exportSchema = false)
abstract class FavDatabase : RoomDatabase(){

    abstract fun favDao(): FavDao
    abstract fun remDao(): ReminderDao

    companion object {
        private var instance: FavDatabase? = null

        @Synchronized
        fun getInstance(context: Context): FavDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context,
                    FavDatabase::class.java, "Favorite"
                ).build()
            }
            return instance as FavDatabase
        }
    }

}