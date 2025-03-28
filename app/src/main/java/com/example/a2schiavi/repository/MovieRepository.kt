package com.example.a2schiavi.repository

import com.example.a2schiavi.model.Movie
import com.example.a2schiavi.model.SearchResponse
import com.example.a2schiavi.network.RetrofitClient

class MovieRepository {
    private val apiKey = "1aa4d575"

    suspend fun searchMovies(query: String): SearchResponse {
        return RetrofitClient.api.searchMovies(query, apiKey)
    }

    suspend fun getMovieDetails(imdbID: String): Movie {
        return RetrofitClient.api.getMovieDetails(imdbID, apiKey)
    }
}
