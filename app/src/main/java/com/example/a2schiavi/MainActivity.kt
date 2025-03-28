package com.example.a2schiavi

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a2schiavi.databinding.ActivityMainBinding
import com.example.a2schiavi.model.MovieItem
import com.example.a2schiavi.ui.MovieAdapter
import com.example.a2schiavi.viewmodel.MovieViewModel

class MainActivity : ComponentActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MovieViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(MovieViewModel::class.java)

        binding.btnSearch.setOnClickListener {
            val query = binding.etSearch.text.toString()
            if (query.isNotBlank()) {
                viewModel.searchMovies(query)
            } else {
                Toast.makeText(this, "Please enter a movie title!", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.movieList.observe(this, Observer { movies ->
            setupRecyclerView(movies)
        })

        viewModel.errorMessage.observe(this, Observer { msg ->
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        })
    }

    private fun setupRecyclerView(movies: List<MovieItem>) {
        val repository = com.example.a2schiavi.repository.MovieRepository()
        val adapter = MovieAdapter(movies, { selected ->
            val intent = Intent(this, DetailsActivity::class.java)
            intent.putExtra("imdbID", selected.imdbID)
            startActivity(intent)
        }, repository)
        binding.rvMovies.layoutManager = LinearLayoutManager(this)
        binding.rvMovies.adapter = adapter
    }
}
