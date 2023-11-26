package com.example.moviedex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.moviedex.movielist.MovieListScreen
import com.example.moviedex.ui.theme.MoviedexTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MoviedexTheme{
                val navController = rememberNavController()
                NavHost(
                    navController = navController ,
                    startDestination = "movie_list_screen"
                ){
                    composable("movie_list_screen"){
                         MovieListScreen(navController = navController)
                    }
                    composable(
                        "movie_detail_screen/{movie_id}",
                        arguments = listOf(
                            navArgument("movie_id"){
                                type = NavType.IntType
                            }
                        )
                    ){
                         val movieId = remember{
                             it.arguments?.getInt("movie_id")
                         }
                    }
                }
            }
        }
    }
}
