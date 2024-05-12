package me.ashutoshkk.lockdown.service

import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class ForegroundAppObserver(private val context: Context) {

    fun flow(): Flow<String> = flow {
        while (true) {
            getForegroundApp()?.let {
                emit(it)
            }
            delay(1000)
        }
    }
        .flowOn(Dispatchers.Default)
        .distinctUntilChanged()

    private fun getForegroundApp(): String? {
        val usageStatsManager =
            context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
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
}