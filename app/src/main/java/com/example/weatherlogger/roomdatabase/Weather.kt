package com.example.weatherlogger.roomdatabase

import android.annotation.SuppressLint
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@SuppressLint("NotConstructor")
@Entity(tableName = "weather")
class Weather(val temp: String,
              val date: String,
              @PrimaryKey(autoGenerate = false) val id: Int? = null) {



//    @PrimaryKey(autoGenerate = true)
//    private var id = 0
//
//    @ColumnInfo(name = "temp")
//    var temp: String? = null
//
//    @ColumnInfo(name = "date")
//    private var date: String? = null
//

//    @Ignore
//    constructor(temp: String?, date: String?) {
//        this.temp = temp
//        this.date = date
//    }


//        constructor() : this("","")




}