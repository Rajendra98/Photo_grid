package com.rajendra.photogrid.domain.repository

import com.rajendra.photogrid.domain.ApiResponse
import com.rajendra.photogrid.domain.model.Photo
import com.rajendra.photogrid.domain.model.SearchResponse

interface PhotoRepository {
    suspend fun getPhotos(page: Int, perPage: Int, accessKey: String): ApiResponse<List<Photo>>
    suspend fun searchPhotos(query: String, accessKey: String):ApiResponse<SearchResponse>
}
