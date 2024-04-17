package com.rajendra.photogrid.data

import com.rajendra.photogrid.domain.model.Photo
import com.rajendra.photogrid.domain.model.SearchResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface UnsplashApi {
    @GET("photos")
  suspend  fun getPhotos(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Query("client_id") clientId: String
    ): List<Photo>

    @GET("search/photos")
   suspend fun searchPhotos(
        @Query("query") query: String,
        @Query("client_id") clientId: String
    ): SearchResponse


    companion object {
        fun create(): UnsplashApi {
            val loggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .connectTimeout(20, java.util.concurrent.TimeUnit.SECONDS) // Increase connection timeout
                .readTimeout(20, java.util.concurrent.TimeUnit.SECONDS) // Increase read timeout
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.unsplash.com/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(UnsplashApi::class.java)

        }
    }
}


