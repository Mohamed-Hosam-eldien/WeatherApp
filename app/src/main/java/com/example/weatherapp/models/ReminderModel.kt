package com.example.weatherapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Reminder")
class ReminderModel(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var dateFrom: String,
    var dateTo: String,var mille1:Long,var mille2:Long) {
}