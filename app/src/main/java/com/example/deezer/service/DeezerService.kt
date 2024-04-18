package com.example.deezer.service

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request


class DeezerService {

    companion object {
        const val TAG = "DeezerService"
        val client = OkHttpClient()
    }

    fun searchArtist(artist: String, callback: CallSearchArtist) {
        Log.d(TAG, "searchArtist: $artist")

        val request: Request = Request.Builder()
            .url("https://api.deezer.com/search/artist?q=$artist")
            .build()

        client.newCall(request).enqueue(callback)
    }


    fun searchAlbum(artistID: String, callback: CallSearchAlbum) {
        Log.d(TAG, "searchAlbum: $artistID")

        val request: Request = Request.Builder()
            .url("https://api.deezer.com/artist/$artistID/albums")
            .build()

        client.newCall(request).enqueue(callback)
    }

    fun searchTrack(albumID: String, callback: CallSearchTrack) {
        Log.d(TAG, "searchTrack: $albumID")

        val request: Request = Request.Builder()
            .url("https://api.deezer.com/album/$albumID/tracks")
            .build()

        Log.d(TAG, "searchTrack: $request")

        client.newCall(request).enqueue(callback)
    }
}