package com.example.moviedex.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteMovie(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val title: String,
    val poster_path: String,
    val overview: String,
    val vote_average: Double,
)
