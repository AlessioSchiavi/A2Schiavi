package com.example.a2schiavi

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.a2schiavi.databinding.ActivityDetailsBinding
import com.example.a2schiavi.viewmodel.MovieViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

class DetailsActivity : ComponentActivity() {

    private lateinit var binding: ActivityDetailsBinding
    private lateinit var viewModel: MovieViewModel

    fun ImageView.loadImageFromUrl(imageUrl: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val connection = URL(imageUrl).openConnection() as HttpURLConnection
                connection.doInput = true
                connection.connect()
                val inputStream = connection.inputStream
                val bitmap = BitmapFactory.decodeStream(inputStream)
                withContext(Dispatchers.Main) {
                    this@loadImageFromUrl.setImageBitmap(bitmap)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(MovieViewModel::class.java)

        val imdbID = intent.getStringExtra("imdbID") ?: ""
        if (imdbID.isEmpty()) {
            Toast.makeText(this, "No IMDb ID provided!", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            viewModel.fetchMovieDetails(imdbID)
        }

        viewModel.selectedMovie.observe(this, Observer { movie ->
            binding.tvTitle.text = movie.Title
            binding.tvYear.text = "Year: ${movie.Year}"
            binding.tvRated.text = "Rated: ${movie.Rated}"
            binding.tvDirector.text = "Director: ${movie.Director}"
            binding.tvGenre.text = "Genre: ${movie.Genre}"
            binding.tvRuntime.text = "Runtime: ${movie.Runtime}"
            binding.tvAwards.text = "Awards: ${movie.Awards}"
            binding.tvPlot.text = "Plot: ${movie.Plot}"

            movie.imdbRating.toFloatOrNull()?.let { rating ->
                binding.ratingBar.rating = rating / 2
            }

            if (movie.Poster.isNotBlank() && movie.Poster != "N/A") {
                binding.ivPoster.loadImageFromUrl(movie.Poster)
            }
        })

        viewModel.errorMessage.observe(this, Observer { error ->
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
        })

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnShare.setOnClickListener {
            viewModel.selectedMovie.value?.let { movie ->
                val imdbLink = "https://www.imdb.com/title/${movie.imdbID}/"
                val shareText = "Check out ${movie.Title}! $imdbLink"
                val shareIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, shareText)
                    type = "text/plain"
                }
                startActivity(Intent.createChooser(shareIntent, "Share via"))
            }
        }
    }
}
