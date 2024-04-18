package com.example.deezer.ui.favori

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.deezer.databinding.RecyclerviewFavoriBinding
import com.example.deezer.persistence.Favori
import com.example.deezer.ui.search.RecyclerArtistAdapter
import com.example.deezer.ui.tracks.RecyclerTrackAdapter

class RecyclerFavoriAdapter(
    private val favoris:List<Favori>,
    private val onDeleteClickListener: (Favori) -> Unit)
    : RecyclerView.Adapter<RecyclerFavoriAdapter.ViewHolder>() {

    private lateinit var navigationController : NavController
    private var mediaPlayer: MediaPlayer? = null

    companion object {
        private const val TAG = "RecyclerTownAdapter"
    }

    inner class ViewHolder(val binding: RecyclerviewFavoriBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.d(TAG, "onCreateViewHolder")

        val binding = RecyclerviewFavoriBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        navigationController = parent.findNavController()
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
       return favoris.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder position=${position}")
        val favori = favoris[position]
        Log.d(RecyclerArtistAdapter.TAG, " ID : ${favori.trackID}")

        try {
            Glide.with(holder.binding.root.context)
                .load(favori.img)
                .into(holder.binding.imageViewFavori)
        } catch (e: Throwable) {
            e.printStackTrace()
            Log.e(TAG, "Glide", e)
        }
        holder.binding.textViewFavoriTrackName.text = "Title : " + favori.trackName
        holder.binding.textViewFavoriAlbumName.text = "Album : " + favori.albumName
        holder.binding.textViewFavoriArtistName.text = "Artist : " + favori.artistName

        holder.binding.likeButton.setOnClickListener {
            onDeleteClickListener(favori)
            notifyItemRemoved(position)
        }

        holder.binding.root.setOnClickListener {
            Log.d(RecyclerTrackAdapter.TAG, "Click on ${favori.trackSound}")

            mediaPlayer?.apply {
                if (isPlaying) {
                    stop()
                }
                release()
            }

            mediaPlayer = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )
                setDataSource(favori.trackSound)
                prepare() // might take long! (for buffering, etc)
                start()
            }
        }
    }
}