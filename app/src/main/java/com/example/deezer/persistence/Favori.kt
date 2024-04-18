package com.example.deezer.persistence

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favori_table")
data class Favori(
    @PrimaryKey
    @ColumnInfo(name = "trackID")
    val trackID: Long,

    @ColumnInfo(name = "trackSound")
    val trackSound: String,

    @ColumnInfo(name = "artistName")
    val artistName: String,

    @ColumnInfo(name = "trackName")
    val trackName: String,

    @ColumnInfo(name = "img")
    val img: String,

    @ColumnInfo(name = "albumName")
    val albumName: String
)