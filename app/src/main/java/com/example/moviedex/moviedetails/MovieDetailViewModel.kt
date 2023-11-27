package com.example.moviedex.moviedetails

import androidx.lifecycle.ViewModel
import com.example.moviedex.data.remote.responses.Movie
import com.example.moviedex.data.remote.responses.MovieDetails
import com.example.moviedex.repository.MovieRepository
import com.example.moviedex.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val repository: MovieRepository
)  : ViewModel() {

    suspend fun getMovieDetail(movieId: Int) : Resource<MovieDetails>{
        return repository.getMovieDetails(movieId)
    }
}