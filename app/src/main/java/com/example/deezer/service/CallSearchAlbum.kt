package com.example.deezer.service

import android.util.Log
import com.example.deezer.service.data.DataSearchAlbum
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

abstract class CallSearchAlbum : Callback {
    companion object{
        private const val TAG = "CallSearchArtist"
    }

    abstract fun fireOnResponseOk(data: DataSearchAlbum)

    override fun onFailure(call: Call, e: IOException) {
        Log.e(DeezerService.TAG, "onFailure: $e")
    }

    override fun onResponse(call: Call, response: Response) {
        val gson = Gson()
        val data: DataSearchAlbum =
            gson.fromJson(response.body?.charStream(), DataSearchAlbum::class.java)
        Log.d(TAG, "onResponse: $data")
        fireOnResponseOk(data)
    }

}