package com.example.moviedex.movielist

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.capitalize
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviedex.data.models.MoviedexListEntry
import com.example.moviedex.repository.MovieRepository
import com.example.moviedex.util.Constants.PAGE_SIZE
import com.example.moviedex.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
 private val repository: MovieRepository
) : ViewModel() {

    private var curPage = 0
    var nextPage = 1 // Add this variable

    var movieList = mutableStateOf<List<MoviedexListEntry>>(listOf())
    var loadError = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    var endReached = mutableStateOf(false)

    private var cachedMovieList = listOf<MoviedexListEntry>()
    private var isSearchStarting = true

    var isSearching = mutableStateOf(false)

    init {
        loadMoviePaginated()
    }

    fun searchMovieList(query: String){
        val listToSearch = if(isSearchStarting){
            movieList.value
        }else{
            cachedMovieList
        }
        viewModelScope.launch(Dispatchers.Default) {
            if (query.isEmpty()){
                movieList.value = cachedMovieList
                isSearching.value = false
                isSearchStarting = true
                return@launch
            }
            val results = listToSearch.filter {
                it.title.contains(query.trim(), ignoreCase = true) ||
                        it.title.contains(query.trim(), ignoreCase = true)
            }
            if (isSearchStarting){
                cachedMovieList = movieList.value
                isSearchStarting = false
            }
            movieList.value = results
            isSearching.value = true
        }
    }

    fun loadMoviePaginated(){
        viewModelScope.launch {
            isLoading.value = true
            val result =repository.getPopularMovies(PAGE_SIZE , curPage, nextPage)
            when (result){
                is Resource.Success ->{
                    endReached.value = nextPage >= result.data!!.total_pages
                    val moviedexEntry = result.data.results.mapIndexed{index,entry ->
                        val title = entry.title
                        val posterUrl = entry.poster_path
                        MoviedexListEntry( title,posterUrl , entry.id)
                    }
                    nextPage++
                    isLoading.value = false
                    loadError.value = ""
                    movieList.value += moviedexEntry
                }
                is Resource.Error ->{
                    loadError.value = result.message!!
                    isLoading.value = false
                }
            }
        }
    }

}