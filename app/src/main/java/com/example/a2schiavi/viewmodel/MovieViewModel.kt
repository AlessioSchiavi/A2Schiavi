package com.example.a2schiavi.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.a2schiavi.model.Movie
import com.example.a2schiavi.model.MovieItem
import com.example.a2schiavi.repository.MovieRepository
import kotlinx.coroutines.launch

class MovieViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = MovieRepository()

    val movieList = MutableLiveData<List<MovieItem>>()

    val selectedMovie = MutableLiveData<Movie>()

    val errorMessage = MutableLiveData<String>()

    fun searchMovies(query: String) {
        viewModelScope.launch {
            try {
                val response = repository.searchMovies(query)
                if (response.Response == "True") {
                    movieList.value = response.Search ?: emptyList()
                } else {
                    errorMessage.value = "No results found for \"$query\"."
                }
            } catch (e: Exception) {
                errorMessage.value = e.message
            }
        }
    }

    fun fetchMovieDetails(imdbID: String) {
        viewModelScope.launch {
            try {
                val movie = repository.getMovieDetails(imdbID)
                selectedMovie.value = movie
            } catch (e: Exception) {
                errorMessage.value = e.message
            }
        }
    }
}
