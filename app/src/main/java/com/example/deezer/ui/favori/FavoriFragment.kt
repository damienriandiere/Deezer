package com.example.deezer.ui.favori

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.deezer.databinding.FragmentFavoriBinding

class FavoriFragment : Fragment() {

    private var _binding: FragmentFavoriBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val favorisViewModel = ViewModelProvider(this).get(FavorisViewModel::class.java)

        _binding = FragmentFavoriBinding.inflate(inflater, container, false)
        binding.recyclerview.layoutManager = LinearLayoutManager(context)
        binding.recyclerview.adapter = RecyclerFavoriAdapter(emptyList(), favorisViewModel::deleteFavori)

        favorisViewModel.state.observe(viewLifecycleOwner) { favoris ->
            binding.recyclerview.post {
                binding.recyclerview.adapter = RecyclerFavoriAdapter(favoris, favorisViewModel::deleteFavori)
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}