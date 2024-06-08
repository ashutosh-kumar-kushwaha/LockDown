package me.ashutoshkk.lockdown.service

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.IBinder
import android.view.WindowManager
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.ashutoshkk.lockdown.data.database.LockedAppDao
import javax.inject.Inject

@AndroidEntryPoint
class AppLockService: Service(), LifecycleOwner, SavedStateRegistryOwner{

    private val _lifecycleRegistry = LifecycleRegistry(this)
    private val _savedStateRegistryController = SavedStateRegistryController.create(this)

    override val lifecycle: Lifecycle = _lifecycleRegistry
    override val savedStateRegistry = _savedStateRegistryController.savedStateRegistry

    @Inject
    lateinit var foregroundAppObserver: ForegroundAppObserver

    @Inject
    lateinit var lockedAppDao: LockedAppDao

    private val lockedApps = mutableSetOf<String>()

    private lateinit var windowManager: WindowManager

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        _savedStateRegistryController.performAttach()
        _savedStateRegistryController.performRestore(null)
        _lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Toast.makeText(applicationContext, "Started", Toast.LENGTH_SHORT).show()
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        observeForegroundApp()
        observeLockedApp()

        return START_STICKY
    }

    private fun observeLockedApp() {
        CoroutineScope(Dispatchers.Default).launch {
            lockedAppDao
                .getLockedApps()
                .collectLatest {
                    lockedApps.clear()
                    lockedApps.addAll(it.map { it.packageName })
                }
        }
    }

    private fun observeForegroundApp() {
        CoroutineScope(Dispatchers.Default).launch {
            foregroundAppObserver
                .asFlow()
                .collectLatest { packageName ->
                    if (lockedApps.find { packageName == it } != null) {
                        withContext(Dispatchers.Main){


                        }
                    } else {

                    }
                }
        }
    }

    private fun showOverlay(){

    }

    override fun onDestroy() {
        super.onDestroy()
        _lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    }
}