package com.example.deezer.ui.album

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.deezer.databinding.FragmentHomeBinding
import com.example.deezer.service.CallSearchAlbum
import com.example.deezer.service.DeezerService
import com.example.deezer.service.data.DataSearchAlbum
import com.example.deezer.service.data.DataSearchArtist

class AlbumFragment : Fragment(){

    companion object{
        private const val TAG = "AlbumFragment"
    }

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.recyclerview.layoutManager = LinearLayoutManager(context)
        binding.recyclerview.adapter = RecyclerAlbumAdapter("","", emptyList())

        arguments?.let {
            val artistID = it.getInt("artistId", -1)
            val artistName = it.getString("artistName", "")
            val artistImage = it.getString("artistImage", "")
            DeezerService().searchAlbum(artistID.toString(), object : CallSearchAlbum() {
                override fun fireOnResponseOk(data: DataSearchAlbum) {
                    Log.d(TAG, "fireOnResponseOk : $data")
                    binding.recyclerview.post {
                        binding.recyclerview.adapter = RecyclerAlbumAdapter(artistName,
                            artistImage,
                            data.data)
                    }
                }
            })
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}