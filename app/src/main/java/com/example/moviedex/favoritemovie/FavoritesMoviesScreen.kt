package com.example.moviedex.favoritemovie

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.moviedex.data.models.FavoriteMovie
import com.example.moviedex.data.models.MoviedexListEntry
import com.example.moviedex.movielist.MoviedexEntry
import com.example.moviedex.ui.theme.LightBlue
import com.example.moviedex.ui.theme.Roboto

@SuppressLint("SuspiciousIndentation")
@Composable
fun FavoritesMoviesScreen(
    navController: NavController,
    favoriteViewModel: FavoriteViewModel = hiltViewModel()
) {
    Column {
        Spacer(modifier = Modifier.height(20.dp))
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
        Text(
            text = "Favorites Movies",
            fontSize = 42.sp,
            color = Color.Blue,
            fontFamily = Roboto,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(15.dp))
        Spacer(modifier = Modifier.height(16.dp))

       MovieList(navController = navController)

    }

}

@Composable
fun MovieList(
    navController: NavController,
    viewModel: FavoriteViewModel = hiltViewModel()
){
    val movieList by viewModel.favoriteMovies.collectAsState(initial = emptyList())


    LazyColumn(contentPadding = PaddingValues(16.dp)){
        val itemCount = if(movieList.size % 2 == 0){
            movieList.size / 2
        }else {
            movieList.size / 2 + 1
        }
        items(itemCount){
            MoviedexRow(
                rowIndex = it,
                entries = movieList,
                navController = navController
            )
        }

    }
}
@Composable
fun MoviedexEntry(
    entry :FavoriteMovie,
    navController: NavController,
    modifier: Modifier = Modifier,
){
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .shadow(5.dp, RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
            .aspectRatio(1f)
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color.White,
                        Color.White
                    )
                )
            )
            .clickable {
                navController.navigate(
                    "movie_detail_screen/${entry.id}"
                )
            }
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {

            AsyncImage(
                model = coil.request.ImageRequest.Builder(LocalContext.current)
                    .data("https://image.tmdb.org/t/p/w500${entry.poster_path}")
                    .build(),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(shape = MaterialTheme.shapes.medium),
            )
            Text(
                text = entry.title,
                fontFamily = Roboto,
                fontSize = 17.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}
@Composable
fun MoviedexRow(
    rowIndex: Int,
    entries: List<FavoriteMovie>,
    navController: NavController,
){
    Column {
        Row {
            MoviedexEntry(
                entry = entries[rowIndex * 2],
                navController = navController,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            if(entries.size >= rowIndex * 2 + 2){
                MoviedexEntry(
                    entry = entries[rowIndex * 2 + 1],
                    navController = navController,
                    modifier = Modifier.weight(1f)
                )
            }else{
                Spacer(modifier = Modifier.weight(1f))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

