package com.rajendra.photogrid.data.repository

import com.rajendra.photogrid.data.UnsplashApi
import com.rajendra.photogrid.domain.ApiResponse
import com.rajendra.photogrid.domain.model.Photo
import com.rajendra.photogrid.domain.model.SearchResponse
import com.rajendra.photogrid.domain.repository.PhotoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PhotoRepositoryImpl(private val api: UnsplashApi) : PhotoRepository {
    override suspend fun getPhotos(page: Int, perPage: Int, accessKey: String): ApiResponse<List<Photo>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getPhotos(page, perPage, accessKey)
                ApiResponse.Success(response)

            } catch (e: Exception) {
                ApiResponse.Failure(e.localizedMessage ?: "Unknown error occurred")
            }
        }
    }

    override suspend fun searchPhotos(query: String, accessKey: String): ApiResponse<SearchResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.searchPhotos(query, accessKey)
                ApiResponse.Success(response)

            } catch (e: Exception) {
                ApiResponse.Failure(e.localizedMessage ?: "Unknown error occurred")
            }
        }
    }
}
