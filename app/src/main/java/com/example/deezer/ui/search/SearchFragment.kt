package com.example.deezer.ui.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.deezer.databinding.FragmentHomeBinding
import com.example.deezer.service.CallSearchArtist
import com.example.deezer.service.DeezerService
import com.example.deezer.service.data.DataSearchArtist

class SearchFragment : Fragment() {

    companion object{
        private const val TAG = "HomeFragment"
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
        binding.recyclerview.adapter = RecyclerArtistAdapter(emptyList())

        arguments?.let {
            val artistName = it.getString("artist", "")
            DeezerService().searchArtist(artistName, object : CallSearchArtist() {
                override fun fireOnResponseOk(data: DataSearchArtist) {
                    Log.d(TAG, "fireOnResponseOk : $data")
                    binding.recyclerview.post {
                        binding.recyclerview.adapter = RecyclerArtistAdapter(data.data)
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