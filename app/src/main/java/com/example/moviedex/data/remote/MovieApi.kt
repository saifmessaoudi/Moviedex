package com.example.moviedex.data.remote

import com.example.moviedex.data.remote.responses.Movie
import com.example.moviedex.data.remote.responses.MovieDetails
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {
    @GET("movie/top_rated")
    suspend fun getPopularMovies(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int ,
        @Query("page") page: Int,
    ): Movie


    @GET("movie/{movie_id}")
    suspend fun getMovie(
        @Path("movie_id") movie_id: Int,
    ): MovieDetails
}