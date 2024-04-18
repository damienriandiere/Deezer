package com.example.deezer.service.data

data class DataSearchArtist(
    val `data`: List<Artist>,
    val next: String,
    val total: Int
)