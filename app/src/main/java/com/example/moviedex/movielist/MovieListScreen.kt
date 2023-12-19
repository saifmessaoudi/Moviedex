package com.example.moviedex.movielist

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage

import com.example.moviedex.R
import com.example.moviedex.data.models.MoviedexListEntry
import com.example.moviedex.ui.theme.Roboto



@Composable
fun MovieListScreen(
    navController : NavController,
    viewModel: MovieListViewModel = hiltViewModel()
){
    Surface(
        color = MaterialTheme.colors.background,
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            Spacer(modifier = Modifier.height(20.dp))
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            
            ) {

                Text(
                    text = "Moviedex",
                    fontSize = 42.sp,
                    color = Color.White,
                    fontFamily = Roboto,
                )
                IconButton(onClick =
                    {
                        navController.navigate("favorites_screen")
                    }
                ) {
                    Icon(
                       imageVector = Icons.Outlined.Favorite,
                        contentDescription = "Favorite Icon",
                        tint = Color.Red
                    )
            }

            }
            Spacer(modifier = Modifier.height(15.dp))
            Image(
                painter = painterResource(id = R.drawable.logo1) ,
                contentDescription = "Movie List Image",
                modifier = Modifier
                    .height(150.dp)
                    .width(150.dp)
                    .align(CenterHorizontally)
            )

            SearchBar(
                hint = "Search...",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ){
                viewModel.searchMovieList(it)
            }
            Spacer(modifier = Modifier.height(16.dp))

            MovieList(navController = navController)

        }
    }
}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    hint: String = "",
    onSearch: (String) -> Unit = {}
){
    var text by remember {
        mutableStateOf("")
    }

    var isHintDisplayed by remember {
        mutableStateOf(hint != "")
    }


    Box(modifier = modifier) {
        BasicTextField(
            value = text,
            onValueChange = {
                text = it
                onSearch(it)
            },
            singleLine = true,
            maxLines = 1,
            textStyle = TextStyle(color = Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .shadow(5.dp, CircleShape)
                .background(Color.White, CircleShape)
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .onFocusChanged {
                    isHintDisplayed = !it.isFocused
                }
            )
        if(isHintDisplayed){
            Text(
                text = hint,
                color = Color.LightGray,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
            )
        }

    }
}

@Composable
fun MovieList(
    navController: NavController,
    viewModel: MovieListViewModel = hiltViewModel()
){
    val movieList by remember {viewModel.movieList}
    val loadError by remember {viewModel.loadError}
    val isLoading by remember {viewModel.isLoading}
    val endReached by remember {viewModel.endReached}
    val isSearching by remember {viewModel.isSearching}

    LazyColumn(contentPadding = PaddingValues(16.dp)){
        val itemCount = if(movieList.size % 2 == 0){
            movieList.size / 2
    }else {
            movieList.size / 2 + 1
        }
        items(itemCount){
             if (it >= itemCount - 1 && !endReached && !isLoading && !isSearching){
                 viewModel.loadMoviePaginated()
             }
            MoviedexRow(rowIndex = it, entries = movieList , navController = navController )
        }
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ){
        if(isLoading){
            CircularProgressIndicator(color = MaterialTheme.colors.primary)
        }
        if(loadError.isNotEmpty()){
            RetrySection(errorMessage = loadError , onRetry = { viewModel.loadMoviePaginated() })
        }
    }
}

@Composable
fun MoviedexEntry(
    entry :MoviedexListEntry,
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
            horizontalAlignment = CenterHorizontally,
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
    entries: List<MoviedexListEntry>,
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

@Composable
fun RetrySection (
    errorMessage: String,
    onRetry: () -> Unit
){
    Column {
       Text(errorMessage, color = Color.Red, fontSize = 18.sp, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { onRetry() },
            modifier = Modifier.align(CenterHorizontally)
        ) {
            Text(text = "Retry")
        }
    }
}

