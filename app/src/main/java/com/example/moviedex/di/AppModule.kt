package com.example.moviedex.di

import android.content.Context
import androidx.room.Database
import com.example.moviedex.data.remote.MovieApi
import com.example.moviedex.repository.FavoriteMovieDao
import com.example.moviedex.repository.MovieRepository
import com.example.moviedex.util.AppDataBase
import com.example.moviedex.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideMovieRepository(
        api: MovieApi
    )= MovieRepository(api)

    @Singleton
    @Provides
    fun provideFavoriteMovieDao(database: AppDataBase): FavoriteMovieDao {
        return database.favoriteMovieDao()
    }
    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDataBase {
        return AppDataBase.getDatabase(context)
    }
    @Singleton
    @Provides
    fun provideMovieApi(): MovieApi {
        val apiKey = "25217e256f714e503209a9c979c7aef2"
        val client = OkHttpClient.Builder().addInterceptor(object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
                val original: Request = chain.request()
                val originalHttpUrl = original.url

                val url = originalHttpUrl.newBuilder()
                    .addQueryParameter("api_key", apiKey)
                    .build()

                val requestBuilder = original.newBuilder()
                    .url(url)

                val request = requestBuilder.build()
                return chain.proceed(request)
            }
        }).build()

        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .baseUrl(BASE_URL)
            .build()
            .create(MovieApi::class.java)
    }
}