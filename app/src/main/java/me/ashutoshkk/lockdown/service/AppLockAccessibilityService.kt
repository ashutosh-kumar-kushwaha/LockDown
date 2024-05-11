package me.ashutoshkk.lockdown.service

import android.app.Service
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.SortedMap
import java.util.TreeMap


class AppLockService: Service() {

    private lateinit var usageStatsManager: UsageStatsManager

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Toast.makeText(applicationContext, "Started", Toast.LENGTH_SHORT).show()
        usageStatsManager = getSystemService(USAGE_STATS_SERVICE) as UsageStatsManager
        startForegroundAppCheck()
        return START_STICKY
    }

    private fun startForegroundAppCheck() {
        CoroutineScope(Dispatchers.IO).launch {
            while(true){
                val time = System.currentTimeMillis()
                val appList: List<UsageStats> =
                    usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 1000, time)
                if (appList.isNotEmpty()) {
                    val mySortedMap: SortedMap<Long, UsageStats> = TreeMap()
                    for (usageStats in appList) {
                        mySortedMap[usageStats.lastTimeUsed] = usageStats
                    }
                    if (!mySortedMap.isEmpty()) {
                        val x = mySortedMap[mySortedMap.lastKey()]!!.packageName
                        withContext(Dispatchers.Main){
//                    Toast.makeText(applicationContext, x, Toast.LENGTH_SHORT).show()
                            Log.d("Ashu", x)
                        }
                    }
                }

                delay(1000)
            }
        }
    }

}