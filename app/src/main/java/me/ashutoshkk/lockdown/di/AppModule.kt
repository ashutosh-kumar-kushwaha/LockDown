package me.ashutoshkk.lockdown.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import me.ashutoshkk.lockdown.data.database.LockDownDatabase
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Provides
    @Singleton
    fun providesLownDownDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context,
            LockDownDatabase::class.java,
            "LockDownDatabase"
        ).build()

    @Provides
    @Singleton
    fun providesLockedAppDao(database: LockDownDatabase) = database.lockedAppDao

}