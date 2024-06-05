package me.ashutoshkk.lockdown.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface LockedAppDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun lockApp(app: LockedApp)

    @Delete
    fun unlockApp(app: LockedApp)

    @Query("SELECT * FROM locked_app")
    fun getLockedApps(): List<LockedApp>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun lockApps(apps: List<LockedApp>)

    @Query("DELETE FROM locked_app")
    fun unlockAllApps()

}