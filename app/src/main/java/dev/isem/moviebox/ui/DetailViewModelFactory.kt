package dev.isem.moviebox.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dev.isem.moviebox.network.MovieProperty

class DetailViewModelFactory(
    private val movieProperty: MovieProperty,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(movieProperty, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}