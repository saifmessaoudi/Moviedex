package com.example.moviedex.moviedetails

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.moviedex.data.remote.responses.MovieDetails
import com.example.moviedex.favoritemovie.FavoriteViewModel
import com.example.moviedex.ui.theme.LightBlue
import com.example.moviedex.ui.theme.Roboto
import com.example.moviedex.util.Resource
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun MovieDetailScreen(
    movieId: Int,
    navController: NavController,
    topPadding: Dp = 20.dp,
    movieImageSize: Dp = 200.dp,
    viewModel: MovieDetailViewModel = hiltViewModel(),
    favoriteViewModel: FavoriteViewModel = hiltViewModel()
) {
    val movieDetail = produceState<Resource<MovieDetails>>(initialValue = Resource.Loading()) {
        value = viewModel.getMovieDetail(movieId)
    }.value


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = LightBlue)
            .padding(bottom = 16.dp)
    ) {

        MovieDetailTopSection(
            navController = navController,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.4f)
                .align(Alignment.TopCenter)
        )
        MovieDetailInfoSection(
            movieDetails = movieDetail,
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = topPadding + movieImageSize / 1f,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 60.dp
                ),
            loading = Modifier
                .size(50.dp)
                .align(Alignment.Center)
        )
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier.fillMaxSize()
        ) {
            if (movieDetail is Resource.Success) {
                movieDetail.data?.let {
                    AsyncImage(
                        model = coil.request.ImageRequest.Builder(LocalContext.current)
                            .data("https://image.tmdb.org/t/p/w500${it.poster_path}")
                            .build(),
                        contentDescription = null,
                        modifier = Modifier
                            .size(movieImageSize)
                            .align(Alignment.TopCenter)
                            .padding(top = topPadding)
                            .offset(y = topPadding)
                    )
                }
            }
        }
    }
}



@Composable
fun MovieDetailTopSection(
    navController: NavController,
    modifier: Modifier = Modifier,
){
    Box(
        contentAlignment = Alignment.TopStart,
        modifier = modifier
            .background(
                Brush.verticalGradient(
                    listOf(
                        androidx.compose.ui.graphics.Color.Transparent,
                       LightBlue
                    )
            ))
    ){
        Icon(
            imageVector= Icons.Default.ArrowBack,
            contentDescription = "Back",
            tint = androidx.compose.ui.graphics.Color.Black,
            modifier = Modifier
                .padding(30.dp)
                .offset(20.dp, 20.dp)
                .clickable {
                    navController.popBackStack()
                }
        )
    }
}

@Composable
fun MovieDetailInfoSection(

    movieDetails: Resource<MovieDetails>,
    modifier: Modifier = Modifier,
    loading: Modifier = Modifier,
){
        when(movieDetails){
            is Resource.Success -> {
                movieDetails.data?.let {
                    Box(
                        modifier = modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = it.title,
                            fontFamily = Roboto,
                            color = androidx.compose.ui.graphics.Color.Black,
                            fontSize = 25.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }

                    Box(
                        modifier = modifier
                            .fillMaxSize()
                            .padding(top = 110.dp)
                            .padding()
                    ) {
                        val genresNames = it.genres.joinToString { genre -> genre.name }
                        Text(
                            text = genresNames,
                            fontFamily = Roboto,
                            color = androidx.compose.ui.graphics.Color.Black,
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 35.dp),

                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = androidx.compose.ui.graphics.Color.Yellow,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = String.format("%.1f", it.vote_average),
                                fontFamily = Roboto,
                                color = androidx.compose.ui.graphics.Color.Black,
                                fontSize = 16.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }
                        Text(
                            text = it.overview,
                            fontFamily = Roboto,
                            color = androidx.compose.ui.graphics.Color.Black,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Justify,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 70.dp)
                        )
                    }
                }
            }
            is Resource.Error -> {
                Text(
                    text = movieDetails.message!!,
                    fontFamily = Roboto,
                    fontSize = 17.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
            is Resource.Loading -> {
                CircularProgressIndicator(
                    color = androidx.compose.ui.graphics.Color.White,
                    modifier = loading
                )
            }
        }
}