package com.example.a2schiavi.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.a2schiavi.databinding.ItemMovieBinding
import com.example.a2schiavi.model.MovieItem
import com.example.a2schiavi.repository.MovieRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MovieAdapter(
    private val movies: List<MovieItem>,
    private val onItemClick: (MovieItem) -> Unit,
    private val repository: MovieRepository
) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    inner class MovieViewHolder(val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: MovieItem) {
            binding.tvName.text = movie.Title
            binding.tvYear.text = movie.Year
            binding.tvMaturityRating.text = "Rated: N/A"
            binding.ratingBar.rating = 0f

            binding.root.setOnClickListener { onItemClick(movie) }

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val details = repository.getMovieDetails(movie.imdbID)
                    withContext(Dispatchers.Main) {
                        binding.tvMaturityRating.text = "Rated: ${details.Rated}"
                        details.imdbRating.toFloatOrNull()?.let { rating ->
                            binding.ratingBar.rating = rating / 2
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        binding.tvMaturityRating.text = "Rated: N/A"
                        binding.ratingBar.rating = 0f
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    override fun getItemCount(): Int = movies.size
}
