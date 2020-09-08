package com.backbase.assignment.network

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieProperty(
    val id: Int,
    @Json(name = "poster_path") val posterSrcUrl: String,
    val title: String,
    @Json(name = "release_date") val releaseDate: String,
    @Json(name = "runtime") val duration: Int?,
    @Json(name = "vote_average") val rating: Double,
    val overview: String,
    @Json(name = "genres") val listOfGenres: List<Genre>?
) : Parcelable

@Parcelize
data class Genre(
    val id: Int,
    val name: String
) : Parcelable

data class MoviesResult(
    @Json(name = "results") val movies: List<MovieProperty>
)
