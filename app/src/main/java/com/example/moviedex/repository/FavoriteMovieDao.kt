package com.example.moviedex.repository

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.moviedex.data.models.FavoriteMovie
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteMovieDao {
     @Query("SELECT * FROM favorites")
     fun getAllFavorites():Flow<List<FavoriteMovie>>

     @Insert(onConflict = OnConflictStrategy.IGNORE)
     suspend fun insertFavorite(movie: FavoriteMovie)

     @Delete
     suspend fun deleteFavorite(movie: FavoriteMovie)

     @Query("SELECT * FROM favorites WHERE id = :id")
     fun getFavoriteById(id: Int): FavoriteMovie

}