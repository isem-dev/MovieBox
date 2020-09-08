package com.backbase.assignment.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.backbase.assignment.databinding.MovieItemBinding
import com.backbase.assignment.network.MovieProperty

class MoviesAdapter(private val onClickListener: OnClickListener) :
    ListAdapter<MovieProperty, MoviesAdapter.MoviePropertyViewHolder>(DiffCallback) {

    class MoviePropertyViewHolder(private var binding: MovieItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(movieProperty: MovieProperty) {
            binding.movie = movieProperty
            binding.executePendingBindings()
        }

    }

    companion object DiffCallback : DiffUtil.ItemCallback<MovieProperty>() {
        override fun areItemsTheSame(oldItem: MovieProperty, newItem: MovieProperty): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: MovieProperty, newItem: MovieProperty): Boolean {
            return oldItem.id == newItem.id
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviePropertyViewHolder {
        return MoviePropertyViewHolder(MovieItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: MoviePropertyViewHolder, position: Int) {
        val movieProperty = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(movieProperty.id)
        }
        holder.bind(movieProperty)
    }

    class OnClickListener(val clickListener: (movieId: Int) -> Unit) {
        fun onClick(movieId: Int) = clickListener(movieId)
    }

}
