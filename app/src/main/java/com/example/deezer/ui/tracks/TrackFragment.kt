package com.example.deezer.ui.tracks

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.deezer.databinding.FragmentHomeBinding
import com.example.deezer.service.CallSearchTrack
import com.example.deezer.service.DeezerService
import com.example.deezer.service.data.DataSearchTrack

class TrackFragment : Fragment(){

    companion object{
        private const val TAG = "TrackFragment"
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
        binding.recyclerview.adapter = RecyclerTrackAdapter("",
            "",
            "",
            emptyList())

        arguments?.let {
            val albumID = it.getInt("albumId", -1)
            val artistName = it.getString("artistName", "")
            val artistImage = it.getString("artistImage", "")
            val albumName = it.getString("albumName", "")
            val albumImage = it.getString("albumImage", "")
            Log.d(TAG, "albumId : $albumID")
            DeezerService().searchTrack(albumID.toString(), object : CallSearchTrack() {
                override fun fireOnResponseOk(data: DataSearchTrack) {
                    Log.d(TAG, "fireOnResponseOk : $data")
                    binding.recyclerview.post {
                        binding.recyclerview.adapter = RecyclerTrackAdapter(artistName,
                            artistImage,
                            albumName,
                            data.data)
                    }
                }
            })
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.recyclerview.adapter = null
        _binding = null
    }
}