package com.rajendra.photogrid.domain.model

data class Photo(
    val urls: Urls
)

data class Urls(
    val small: String,
    val medium:String,
    val regular:String
)
data class SearchResponse(val results: List<Photo>)
