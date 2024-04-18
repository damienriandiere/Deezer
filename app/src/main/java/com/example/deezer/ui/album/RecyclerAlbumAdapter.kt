package com.example.deezer.ui.album

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.deezer.R
import com.example.deezer.databinding.RecyclerviewAlbumBinding
import com.example.deezer.service.data.Album

class RecyclerAlbumAdapter(private val artistName : String,
                           private val artistImage : String,
                           private val albums: List<Album>) :
    RecyclerView.Adapter<RecyclerAlbumAdapter.ViewHolder>(){

    private lateinit var navigationController : NavController

    companion object {
        private const val TAG = "RecyclerAlbumAdapter"
    }

    inner class ViewHolder(val binding: RecyclerviewAlbumBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.d(TAG, "onCreateViewHolder")
        val binding = RecyclerviewAlbumBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        navigationController = parent.findNavController()
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder position=${position}")

        val album = albums[position]
        Log.d(TAG, " title ${album.cover}")
        try {
            Glide.with(holder.binding.root.context)
                .load(album.cover_medium)
                .into(holder.binding.imageViewAlbum)
        } catch (e: Throwable) {
            e.printStackTrace()
            Log.e(TAG, "Glide", e)
        }
        holder.binding.textViewAlbumTitle.text = album.title
        holder.binding.textViewAlbumFollowers.text = album.fans.toString() + " followers."

        holder.binding.root.setOnClickListener {
            Log.d(TAG, "Click on ${album.cover}")
            val bundle = bundleOf()
            bundle.putInt("albumId", album.id)
            bundle.putString("artistName", artistName)
            bundle.putString("artistImage", artistImage)
            bundle.putString("albumName", album.title)
            bundle.putString("albumImage", album.cover_medium)
            navigationController.navigate(R.id.navigation_track, bundle)
        }
    }

    override fun getItemCount(): Int {
        return albums.size
    }
}