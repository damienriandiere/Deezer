package com.example.deezer

import android.app.Application
import android.content.Context
import android.util.Log
import com.example.deezer.persistence.FavoriDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class AppApplication: Application() {
    companion object {
        const val TAG : String = "AppApplication"
        lateinit var database: FavoriDatabase
        lateinit var context: Context private set
        val applicationScope = CoroutineScope(SupervisorJob())
    }

    // No need to cancel this scope as it'll be torn down with the process

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        database = FavoriDatabase.getDatabase(applicationContext)
    }
}