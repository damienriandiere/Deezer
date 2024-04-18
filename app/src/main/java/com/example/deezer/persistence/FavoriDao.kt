package com.example.deezer.persistence

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface FavoriDao {

    @Query("SELECT * FROM favori_table ORDER BY trackID ASC")
    fun getFavoris(): LiveData<List<Favori>>

    @Query("SELECT * from favori_table WHERE trackID = :id")
    fun getFavori(id: Int): Favori

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(favori: Favori)

    @Update
    suspend fun update(favori: Favori)

    @Query("DELETE FROM favori_table WHERE trackID = :id")
    suspend fun delete(id: Long)

    @Query("DELETE FROM favori_table")
    suspend fun deleteAll()
}