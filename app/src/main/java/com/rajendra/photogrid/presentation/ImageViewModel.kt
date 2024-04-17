package com.rajendra.photogrid.presentation

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rajendra.photogrid.Config
import com.rajendra.photogrid.data.ImageLoader
import com.rajendra.photogrid.data.UnsplashApi
import com.rajendra.photogrid.domain.ApiResponse
import com.rajendra.photogrid.domain.model.Photo
import com.rajendra.photogrid.domain.model.SearchResponse
import com.rajendra.photogrid.domain.repository.PhotoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ImageViewModel(private val photoRepository: PhotoRepository , private val imageLoader: ImageLoader, private val clientId: String) : ViewModel() {

    private val _state = MutableStateFlow(MainState(photos = emptyList()))
    val state = _state.asStateFlow()

    private var currentPage = 1

    init {
        loadPhotos()
    }

    fun handleEvent(mainEvent: MainEvent)
    {
        when(mainEvent)
        {
            is MainEvent.ScrollDown -> {
                currentPage++
                loadPhotos()
            }
            is MainEvent.Search -> {
                searchPhotos(mainEvent.query)
            }
        }
    }


private  fun loadPhotos() {
        viewModelScope.launch {


            _state.value=_state.value.copy(loading = true, error = null)
            val response=photoRepository.getPhotos(currentPage,30,Config.AccessKey)
            when(response)
            {
                is ApiResponse.Failure -> {

                    _state.value=_state.value.copy(loading = false, error = response.message)

                }
                is ApiResponse.Loading -> {}
                is ApiResponse.Success -> {
                    val photos=state.value.photos
                    _state.value=_state.value.copy(loading = false, error = null, photos = photos+response.data)

                }
            }

        }
    }
 private   fun searchPhotos(query: String) {

        viewModelScope.launch {
            _state.value=_state.value.copy(loading = true, error = null, photos = emptyList())

            when(val response=photoRepository.searchPhotos(query,Config.AccessKey))
            {
                is ApiResponse.Failure -> {
                    _state.value=_state.value.copy(loading = false, error = response.message)

                }
                is ApiResponse.Loading -> {}
                is ApiResponse.Success -> {
                    _state.value=_state.value.copy(loading = false, error = null, photos = response.data.results)

                }
            }
        }
    }


    // Function to load an image using ImageLoader
    suspend fun loadImage(url: String): Bitmap? {
        return imageLoader.loadImage(url)
    }
}


data class MainState(
    val photos: List<Photo>,
    val loading: Boolean=false,
    val error: String?=null,
)

sealed class MainEvent{
    data class Search(val query: String):MainEvent()
    object ScrollDown:MainEvent()

}



