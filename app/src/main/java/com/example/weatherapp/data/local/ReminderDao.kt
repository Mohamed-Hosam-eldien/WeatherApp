package com.example.weatherapp.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherapp.models.ReminderModel

@Dao
interface ReminderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRem(reminderModel: ReminderModel)

    @Query("select * from Reminder")
    fun getAllRem() : LiveData<List<ReminderModel>>

    @Query("delete from Reminder where id=:id")
    suspend fun deleteFromRem(id:Int)
}