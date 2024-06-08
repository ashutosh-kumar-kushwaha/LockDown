package me.ashutoshkk.lockdown.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [LockedApp::class],
    version = 1
)
abstract class LockDownDatabase: RoomDatabase() {

    abstract val lockedAppDao: LockedAppDao

}