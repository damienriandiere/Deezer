package com.example.deezer.service.data

data class DataSearchAlbum(
    val `data`: List<Album>,
    val next: String,
    val total: Int
)