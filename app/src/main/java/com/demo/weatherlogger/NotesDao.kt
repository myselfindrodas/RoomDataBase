package com.demo.weatherlogger

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NotesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(weather :Weather)

    @Update
    suspend fun update(weather: Weather)

    @Delete
    suspend fun delete(weather: Weather)

   @Query("DELETE FROM notesTable")
   suspend fun deleteAll()

    @Query("Select * from notesTable order by id ASC")
    fun getAllNotes(): LiveData<List<Weather>>


}