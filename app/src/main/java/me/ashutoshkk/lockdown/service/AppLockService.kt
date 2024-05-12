package me.ashutoshkk.lockdown.service

import android.app.Service
import android.app.usage.UsageEvents
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.SortedMap
import java.util.TreeMap


class AppLockService : Service() {

    private lateinit var usageStatsManager: UsageStatsManager

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Toast.makeText(applicationContext, "Started", Toast.LENGTH_SHORT).show()
        usageStatsManager = getSystemService(USAGE_STATS_SERVICE) as UsageStatsManager
        val foregroundAppObserver = ForegroundAppObserver(applicationContext)
        CoroutineScope(Dispatchers.Default).launch {
            foregroundAppObserver
                .flow()
                .collectLatest {
                    Log.d("Ashu", it)
                }
        }
        return START_STICKY
    }
}