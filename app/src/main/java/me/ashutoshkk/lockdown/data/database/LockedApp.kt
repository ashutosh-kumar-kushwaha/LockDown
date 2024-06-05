package me.ashutoshkk.lockdown.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "locked_app")
data class LockedApp(
    @PrimaryKey
    val packageName: String
)