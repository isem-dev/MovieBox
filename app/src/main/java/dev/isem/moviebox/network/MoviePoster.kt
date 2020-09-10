package dev.isem.moviebox.network

import com.squareup.moshi.Json

data class MoviePoster(
    val id: Int,
    @Json(name = "poster_path") val posterSrcUrl: String
)

data class PostersResult(
    @Json(name = "results") val movies: List<MoviePoster>
)
