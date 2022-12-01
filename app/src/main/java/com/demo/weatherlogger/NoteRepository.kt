package com.demo.weatherlogger

import androidx.lifecycle.LiveData

class NoteRepository(private val notesDao: NotesDao) {


    val allNotes: LiveData<List<Weather>> = notesDao.getAllNotes()

    suspend fun insert(weather: Weather) {
        notesDao.insert(weather)
    }
    suspend fun delete(weather: Weather){
        notesDao.delete(weather)
    }

    suspend fun deleteAll(){
        notesDao.deleteAll()
    }

    suspend fun update(weather: Weather){
        notesDao.update(weather)
    }
}