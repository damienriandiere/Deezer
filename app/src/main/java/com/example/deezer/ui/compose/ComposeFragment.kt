package com.example.deezer.ui.compose

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import coil.compose.rememberImagePainter
import com.example.deezer.AppApplication
import com.example.deezer.databinding.FragmentComposeBinding
import com.example.deezer.persistence.Favori
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ComposeFragment : Fragment(), LifecycleObserver {

    private var _binding: FragmentComposeBinding? = null
    private val binding get() = _binding!!
    private lateinit var lifecycleScope: CoroutineScope

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentComposeBinding.inflate(inflater, container, false)

        lifecycleScope = CoroutineScope(Dispatchers.Main)

        // Ajouter le fragment comme observateur du cycle de vie
        viewLifecycleOwner.lifecycle.addObserver(this)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        // Initialiser votre base de données et observer les changements de la liste de favoris
        observeFavorisList()
    }

    private fun observeFavorisList() {
        // Observez les changements de la liste de favoris
        AppApplication.database.favoriDao().getFavoris().observe(viewLifecycleOwner) { favoris ->
            updateFavorisList(favoris)
        }
    }

    private fun updateFavorisList(favoris: List<Favori>) {
        // Mettre à jour la liste de favoris dans l'UI
        binding.composeView.setContent {
            FavorisList(favoris)
        }
    }

    @Composable
    fun FavorisList(favoris: List<Favori>) {
        Column {
            favoris.forEach { favori ->

                FavoriItem(favori = favori, onDeleteClickListener = { favoriToDelete ->
                    lifecycleScope.launch {
                        AppApplication.database.favoriDao().delete(favori.trackID)
                    }
                })
            }
        }
    }

    @Composable
    fun FavoriItem(favori: Favori, onDeleteClickListener: (Favori) -> Unit) {
        val mediaPlayer = remember { MediaPlayer() }

        Card {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row {
                    val imagePainter = rememberImagePainter(favori.img)
                    Image(
                        painter = imagePainter,
                        contentDescription = null,
                        modifier = Modifier.size(50.dp), // Définissez la taille de l'image selon vos besoins
                        contentScale = ContentScale.FillWidth
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Title: ${favori.trackName}")
                        Text(text = "Album: ${favori.albumName}")
                        Text(text = "Artist: ${favori.artistName}")
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    Button(onClick = { onDeleteClickListener(favori) }) {
                        Text(text = "Delete")
                    }
                    Button(onClick = {
                        mediaPlayer.apply {
                            if (isPlaying) {
                                stop()
                            }
                            reset()
                            try {
                                setDataSource(favori.trackSound)
                                setAudioAttributes(
                                    AudioAttributes.Builder()
                                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                        .setUsage(AudioAttributes.USAGE_MEDIA)
                                        .build()
                                )
                                prepare()
                                start()
                            } catch (e: Exception) {
                                Log.e("FavoriItem", "Error playing audio: ${e.message}", e)
                            }
                        }
                    }) {
                        Text(text = "Play")
                    }
                }
            }
        }
    }
}