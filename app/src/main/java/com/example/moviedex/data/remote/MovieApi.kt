package com.example.moviedex.data.remote

import com.example.moviedex.data.remote.responses.Movie
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApi {
    @GET("movie/top_rated")
    suspend fun getPopularMovies(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int ,
        @Query("page") page: Int,
    ): Movie
}