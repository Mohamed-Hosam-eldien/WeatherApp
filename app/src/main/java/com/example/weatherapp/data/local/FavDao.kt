package com.example.weatherapp.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(favModel: FavModel)

    @Query("select * from Favorite")
    fun getAllFav() : LiveData<List<FavModel>>


    @Query("delete from Favorite where id=:id")
    suspend fun deleteFromFav(id:Int)
}