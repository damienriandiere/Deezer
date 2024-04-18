package com.example.deezer.ui.tracks

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.deezer.AppApplication
import com.example.deezer.R
import com.example.deezer.databinding.RecyclerviewTrackBinding
import com.example.deezer.persistence.Favori
import com.example.deezer.service.data.Track
import com.example.deezer.ui.album.RecyclerAlbumAdapter
import kotlinx.coroutines.launch

class RecyclerTrackAdapter(private val artistName : String,
                           private val artistImage : String,
                           private val albumName : String,
                           private val tracks: List<Track>) :
    RecyclerView.Adapter<RecyclerTrackAdapter.ViewHolder>(){

    private lateinit var navigationController : NavController
    var mediaPlayer: MediaPlayer? = null

    companion object {
        const val TAG = "RecyclerTrackAdapter"
    }

    inner class ViewHolder(val binding: RecyclerviewTrackBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.d(TAG, "onCreateViewHolder")
        val binding = RecyclerviewTrackBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        navigationController = parent.findNavController()
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder position=$position")

        val track = tracks[position]
        Log.d(TAG, " title ${track.title}")

        holder.binding.textViewTrackTitle.text = track.title
        val minutes = track.duration / 60
        val seconds = track.duration % 60
        if (minutes < 1){
            holder.binding.textViewTrackDuration.text = seconds.toString() + " s "
        } else {
            holder.binding.textViewTrackDuration.text = minutes.toString() + " m " + seconds.toString() + " s "
        }

        holder.binding.likeButton.setOnClickListener {
            if (holder.binding.likeButton.tag == "empty") {
                holder.binding.likeButton.setImageResource(R.drawable.heart_full)
                holder.binding.likeButton.tag = "full"
                Log.d(TAG, "Track ID: ${track.id}")
                Log.d(TAG, "Track Preview: ${track.preview}")
                Log.d(TAG, "Artist Name: $artistName")
                Log.d(TAG, "Track Title: ${track.title}")
                Log.d(TAG, "Artist Image: $artistImage")
                Log.d(TAG, "Album Name: $albumName")
                AppApplication.applicationScope.launch {
                    val favori = Favori(
                        track.id,
                        track.preview,
                        artistName,
                        track.title,
                        artistImage,
                        albumName
                    )
                    AppApplication.database.favoriDao().insert(favori)
                }
            } else {
                holder.binding.likeButton.setImageResource(R.drawable.heart_empty)
                holder.binding.likeButton.tag = "empty"
                AppApplication.applicationScope.launch{
                    AppApplication.database.favoriDao().delete(track.id)
                }
            }
        }

        holder.binding.root.setOnClickListener {
            Log.d(TAG, "Click on ${track.title}")

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
                setDataSource(track.preview)
                prepare() // might take long! (for buffering, etc)
                start()
            }
        }
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

}