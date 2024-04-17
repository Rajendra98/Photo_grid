package com.rajendra.photogrid.domain

sealed class ApiResponse<out T> {
    class Loading<out T> : ApiResponse<T>()
    data class Success<out T>(val data: T) : ApiResponse<T>()
    data class Failure(val message: String? = null) : ApiResponse<Nothing>()
}
