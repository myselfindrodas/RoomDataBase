package com.demo.weatherlogger

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteViewModal(application: Application) : AndroidViewModel(application) {
    val allNotes: LiveData<List<Weather>>
    val repository: NoteRepository

    init {
        val dao = NoteDatabase.getDatabase(application).getNotesDao()
        repository = NoteRepository(dao)
        allNotes = repository.allNotes
    }

    fun deleteNote(weather: Weather) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(weather)
    }

    fun deleteAllNote() = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteAll()
    }


    fun updateNote(weather: Weather) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(weather)
    }

    fun addNote(weather: Weather) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(weather)
    }
}