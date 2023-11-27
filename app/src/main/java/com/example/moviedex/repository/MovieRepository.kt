package com.example.moviedex.repository

import com.example.moviedex.data.remote.MovieApi
import com.example.moviedex.data.remote.responses.Movie
import com.example.moviedex.data.remote.responses.MovieDetails
import com.example.moviedex.util.Resource
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class MovieRepository @Inject constructor(
    private val movieApi: MovieApi
) {
    suspend fun getPopularMovies(limit: Int, offset: Int , page :Int) : Resource<Movie> {
        val response = try{
            movieApi.getPopularMovies(limit, offset , page)
        }catch (e: Exception){
            return Resource.Error("An unknown error occured !")
         }
        return Resource.Success(response)
    }

    suspend fun getMovieDetails(movieId : Int) : Resource<MovieDetails> {
        val response = try{
            movieApi.getMovie(movieId)
        }catch (e: Exception){
            return Resource.Error("An unknown error occured !")
        }
        return Resource.Success(response)
    }

}