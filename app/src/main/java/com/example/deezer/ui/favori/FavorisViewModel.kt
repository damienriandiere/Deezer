package com.example.deezer.ui.favori

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.deezer.AppApplication
import com.example.deezer.persistence.Favori
import kotlinx.coroutines.launch

class FavorisViewModel : ViewModel() {
    var state: LiveData<List<Favori>> = AppApplication.database.favoriDao().getFavoris()

    fun deleteFavori(favori: Favori) {
        AppApplication.applicationScope.launch {
            AppApplication.database.favoriDao().delete(favori.trackID)
        }
    }
}