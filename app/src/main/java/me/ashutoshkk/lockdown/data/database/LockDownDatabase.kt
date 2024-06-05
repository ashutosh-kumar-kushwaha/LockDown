package me.ashutoshkk.lockdown.data.database

import androidx.room.Database

@Database(
    entities = [LockedApp::class],
    version = 1
)
abstract class LockDownDatabase {

    abstract val lockedAppDao: LockedAppDao

}