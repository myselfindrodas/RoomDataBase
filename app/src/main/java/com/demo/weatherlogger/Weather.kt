package com.demo.weatherlogger

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notesTable")
class Weather (@ColumnInfo(name = "temp")val temp :String,
               @ColumnInfo(name = "date")val date :String) {
    @PrimaryKey(autoGenerate = true) var id = 0

}