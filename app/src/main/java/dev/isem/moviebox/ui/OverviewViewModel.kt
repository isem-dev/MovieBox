package dev.isem.moviebox.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.isem.moviebox.BuildConfig
import kotlinx.coroutines.*
import dev.isem.moviebox.network.MoviePoster
import dev.isem.moviebox.network.MovieProperty
import dev.isem.moviebox.network.TmdbApi

enum class TmdbApiStatus { PROGRESS, DONE, ERROR}

class OverviewViewModel : ViewModel() {

    private val _status = MutableLiveData<TmdbApiStatus>()
    val status: LiveData<TmdbApiStatus>
        get() = _status

    private val _moviesNowPlaying = MutableLiveData<List<MoviePoster>>()
    val moviesNowPlaying: LiveData<List<MoviePoster>>
        get() = _moviesNowPlaying

    private val _moviesPopular = MutableLiveData<List<MovieProperty>>()
    val moviesPopular: LiveData<List<MovieProperty>>
        get() = _moviesPopular

    private val _navigateToSelectedMovie = MutableLiveData<MovieProperty>()
    val navigateToSelectedMovie: LiveData<MovieProperty>
        get() = _navigateToSelectedMovie

    private val overviewJob: CompletableJob = Job()
    private val coroutineScope: CoroutineScope = CoroutineScope(overviewJob + Dispatchers.Main)

    init {
        getMoviesNowPlaying()
        getMoviesPopular()
    }

    private fun getMoviesNowPlaying() {
        coroutineScope.launch {
            try {
                _status.value = TmdbApiStatus.PROGRESS
                val result = TmdbApi.retrofitService.getNowPlaying(BuildConfig.API_KEY)
                _status.value = TmdbApiStatus.DONE
                _moviesNowPlaying.value = result.movies
            } catch (e: Exception) {
                _status.value = TmdbApiStatus.ERROR
                _moviesNowPlaying.value = ArrayList()
            }
        }
    }

    private fun getMoviesPopular() {
        coroutineScope.launch {
            try {
                _status.value = TmdbApiStatus.PROGRESS
                val result = TmdbApi.retrofitService.getPopular(BuildConfig.API_KEY)
                _status.value = TmdbApiStatus.DONE
                _moviesPopular.value = result.movies
            } catch (e: Exception) {
                _status.value = TmdbApiStatus.ERROR
                _moviesPopular.value = ArrayList()
            }
        }
    }

    private fun getMovieDetails(movieId: Int) {
        coroutineScope.launch {
            try {
                _status.value = TmdbApiStatus.PROGRESS
                val result = TmdbApi.retrofitService.getMovieDetails(movieId, BuildConfig.API_KEY)
                _status.value = TmdbApiStatus.DONE
                _navigateToSelectedMovie.value = result
            } catch (e: java.lang.Exception) {
                Log.e("getMovieDetails", e.message)
                _status.value = TmdbApiStatus.ERROR
            }
        }
    }

    fun displayMovieDetails(movieId: Int) {
        getMovieDetails(movieId)
    }

    fun displayMovieDetailsComplete() {
        _navigateToSelectedMovie.value = null
    }

    override fun onCleared() {
        super.onCleared()
        overviewJob.cancel()
    }

}
