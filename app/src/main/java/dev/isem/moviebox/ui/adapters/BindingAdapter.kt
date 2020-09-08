package com.backbase.assignment.ui.adapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.backbase.assignment.R
import com.backbase.assignment.network.Genre
import com.backbase.assignment.network.MoviePoster
import com.backbase.assignment.network.MovieProperty
import com.backbase.assignment.ui.TmdbApiStatus
import com.backbase.assignment.ui.custom.RatingView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.text.SimpleDateFormat
import java.util.*

private const val BASE_IMAGE_URL = "https://image.tmdb.org/t/p/original/"

@BindingAdapter("gridNowPlayingData")
fun bindRecyclerViewNowPlaying(recyclerView: RecyclerView, nowPlayingData: List<MoviePoster>?) {
    val adapter = recyclerView.adapter as PostersGridAdapter
    adapter.submitList(nowPlayingData)
}

@BindingAdapter("listPopularData")
fun bindRecyclerViewPopular(recyclerView: RecyclerView, popularData: List<MovieProperty>?) {
    val adapter = recyclerView.adapter as MoviesAdapter
    adapter.submitList(popularData)
}

@BindingAdapter("releaseDateFormat")
fun TextView.formatReleaseDate(releaseDate: String?) {
    releaseDate.let {
        text = SimpleDateFormat("MMMM dd, yyyy")
            .format(SimpleDateFormat("yyyy-MM-dd").parse(releaseDate))
    }
}

@BindingAdapter("movieRating")
fun RatingView.bindRating(rating: Double) {
    setRating(rating * 10)
}

@BindingAdapter("genres")
fun TextView.bindGenres(genres: List<Genre>) {
    val list = mutableListOf("")
    genres.forEach {
        list.add(it.name)
    }
    text = list.joinToString(separator = " | ", postfix = " |").toUpperCase(Locale.ROOT)
}

@BindingAdapter("durationTimeFormat")
fun TextView.formatDurationTime(duration: Int?) {
    duration.let {
        val integer = duration?.div(60) ?: 0
        val remainder = duration?.rem(60)
        text = "${integer}h ${remainder}m"
    }
}

@BindingAdapter("posterUrl")
fun bindImage(imageView: ImageView, imageUrl: String?) {
    imageUrl?.let {
        val imageUri = "$BASE_IMAGE_URL$it".toUri().buildUpon().scheme("https").build()
        Glide.with(imageView.context)
            .load(imageUri)
            .apply(
                RequestOptions()
                .placeholder(R.drawable.loading_animation)
                .error(R.drawable.ic_broken_image))
            .into(imageView)
    }
}

@BindingAdapter("apiStatus")
fun bindStatus(statusImageView: ImageView, status: TmdbApiStatus?) {
    when (status) {
        TmdbApiStatus.PROGRESS -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.loading_animation)
        }
        TmdbApiStatus.ERROR -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.ic_connection_error)
        }
        TmdbApiStatus.DONE -> {
            statusImageView.visibility = View.GONE
        }
    }
}