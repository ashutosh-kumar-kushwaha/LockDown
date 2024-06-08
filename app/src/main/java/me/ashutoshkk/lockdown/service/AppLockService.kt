package me.ashutoshkk.lockdown.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.ashutoshkk.lockdown.data.database.LockedApp
import me.ashutoshkk.lockdown.data.database.LockedAppDao
import javax.inject.Inject

@AndroidEntryPoint
class AppLockService : Service() {

    @Inject
    lateinit var foregroundAppObserver: ForegroundAppObserver

    @Inject
    lateinit var lockedAppDao: LockedAppDao

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Toast.makeText(applicationContext, "Started", Toast.LENGTH_SHORT).show()
        observeForegroundApp()
        observeLockedApp()

        return START_STICKY
    }

    fun observeLockedApp(){
        CoroutineScope(Dispatchers.Default).launch {
            lockedAppDao
               .getLockedApps()
               .collectLatest {
                    Log.d("Ashu", it.toString())
               }
        }
    }

    fun observeForegroundApp(){
        CoroutineScope(Dispatchers.Default).launch {
            foregroundAppObserver
                .asFlow()
                .collectLatest {
                    Log.d("Ashu", it)
                }
        }
    }
}