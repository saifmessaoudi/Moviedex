package com.example.moviedex.favoritemovie

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.moviedex.data.models.FavoriteMovie
import com.example.moviedex.data.remote.responses.MovieDetails
import com.example.moviedex.repository.FavoriteMovieDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val favoriteMovieDao: FavoriteMovieDao
)  : ViewModel() {
    val favoriteMovies: Flow<List<FavoriteMovie>> = favoriteMovieDao.getAllFavorites()

     fun isFavorite(movie: MovieDetails): Boolean {
     if (favoriteMovieDao.getFavoriteById(movie.id) != null) {
         return true
     }
         return false
    }

    suspend fun addFavorite(movie: MovieDetails) {
        val favoriteMovie = FavoriteMovie(
            id = movie.id,
            title = movie.title,
            overview = movie.overview,
            poster_path = movie.poster_path ?: "",
            vote_average = movie.vote_average ?: 0.0
        )
        favoriteMovieDao.insertFavorite(favoriteMovie)
        Log.d("FavoriteViewModel", "Adding to favorites: ${movie.title}")

    }

    suspend fun removeFavorite(movie: MovieDetails) {
        val favoriteMovie = FavoriteMovie(
            id = movie.id,
            title = movie.title,
            overview = movie.overview,
            poster_path = movie.poster_path ?: "",
            vote_average = movie.vote_average ?: 0.0
        )
        favoriteMovieDao.deleteFavorite(favoriteMovie)
    }

}