package com.example.a2schiavi.network

import com.example.a2schiavi.model.Movie
import com.example.a2schiavi.model.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface OMDbApi {

    @GET("/")
    suspend fun searchMovies(
        @Query("s") query: String,
        @Query("apikey") apiKey: String,
        @Query("page") page: Int = 1
    ): SearchResponse

    @GET("/")
    suspend fun getMovieDetails(
        @Query("i") imdbID: String,
        @Query("apikey") apiKey: String,
        @Query("plot") plot: String = "full"
    ): Movie
}
