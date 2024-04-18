package com.example.deezer.ui.search

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.deezer.R
import com.example.deezer.databinding.RecyclerviewArtistBinding
import com.example.deezer.service.data.Artist

class RecyclerArtistAdapter(private val artists: List<Artist>) :
    RecyclerView.Adapter<RecyclerArtistAdapter.ViewHolder>(){

    private lateinit var navigationController : NavController

        companion object {
            const val TAG = "RecyclerArtistAdapter"
        }

    inner class ViewHolder(val binding: RecyclerviewArtistBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.d(TAG, "onCreateViewHolder")
        val binding = RecyclerviewArtistBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        navigationController = parent.findNavController()
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder position=${position}")

        val artist = artists[position]
        Log.d(TAG, " picture ${artist.picture}")
        try {
            Glide.with(holder.binding.root.context)
                .load(artist.picture_medium)
                .into(holder.binding.imageViewArtist)
        } catch (e: Throwable) {
            e.printStackTrace()
            Log.e(TAG, "Glide", e)
        }
        holder.binding.textViewArtistName.text = artist.name
        holder.binding.textViewArtistFollowers.text = artist.nb_fan.toString() + " followers."

        holder.binding.root.setOnClickListener {
            Log.d(TAG, "Click on ${artist.name}")
            val bundle = bundleOf()
            bundle.putInt("artistId", artist.id)
            bundle.putString("artistName", artist.name)
            bundle.putString("artistImage", artist.picture_medium)
            navigationController.navigate(R.id.navigation_album, bundle)
        }
    }

    override fun getItemCount(): Int {
        return artists.size
    }

}