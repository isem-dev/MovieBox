package dev.isem.moviebox.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.isem.moviebox.databinding.NowPlayingGridItemBinding
import dev.isem.moviebox.network.MoviePoster

class PostersGridAdapter(private val onClickListener: OnClickListener) :
    ListAdapter<MoviePoster, PostersGridAdapter.MoviePosterViewHolder>(DiffCallback) {

    class MoviePosterViewHolder(private var binding: NowPlayingGridItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(moviePoster: MoviePoster) {
            binding.poster = moviePoster
            binding.executePendingBindings()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<MoviePoster>() {
        override fun areItemsTheSame(oldItem: MoviePoster, newItem: MoviePoster): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: MoviePoster, newItem: MoviePoster): Boolean {
            return oldItem.id == newItem.id
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviePosterViewHolder {
        return MoviePosterViewHolder(NowPlayingGridItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: MoviePosterViewHolder, position: Int) {
        val moviePoster = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(moviePoster.id)
        }
        holder.bind(moviePoster)
    }

    class OnClickListener(val clickListener: (movieId: Int) -> Unit) {
        fun onClick(movieId: Int) = clickListener(movieId)
    }
}
