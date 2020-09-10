package dev.isem.moviebox.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dev.isem.moviebox.network.MovieProperty

class DetailViewModel(movieProperty: MovieProperty, app: Application) : AndroidViewModel(app) {

    private val _selectedMovie = MutableLiveData<MovieProperty>()
    val selectedMovie: LiveData<MovieProperty>
        get() = _selectedMovie

    init {
        _selectedMovie.value = movieProperty
    }
}
