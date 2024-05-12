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
//        startForegroundAppCheck()
        return START_STICKY
    }

    private fun startForegroundAppCheck() {
        CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                withContext(Dispatchers.Main) {
                    Log.d("Ashu", getForegroundApp().toString())
                }
                delay(1000)
            }
        }
    }

    private fun getForegroundApp2(): String? {
        val time = System.currentTimeMillis()
        val appList: List<UsageStats> =
            usageStatsManager.queryUsageStats(
                UsageStatsManager.INTERVAL_DAILY,
                time - 1000 * 1000,
                time
            )
        if (appList.isNotEmpty()) {
            val mySortedMap: SortedMap<Long, UsageStats> = TreeMap()
            for (usageStats in appList) {
                mySortedMap[usageStats.lastTimeUsed] = usageStats
            }
            if (!mySortedMap.isEmpty()) {
                return mySortedMap[mySortedMap.lastKey()]!!.packageName
            }
        }
        return null
    }

    private fun getForegroundApp(): String? {
        val usageStatsManager = getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val currentTime = System.currentTimeMillis()
        val events = usageStatsManager.queryEvents(currentTime - 10000, currentTime)
        val event = UsageEvents.Event()
        var packageName: String? = null

        while (events.hasNextEvent()) {
            events.getNextEvent(event)
            if (event.eventType == UsageEvents.Event.ACTIVITY_RESUMED) {
                packageName = event.packageName
            }
        }
        return packageName
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)

    }

}