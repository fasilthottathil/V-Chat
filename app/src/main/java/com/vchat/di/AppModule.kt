package com.vchat.di

import android.app.Application
import android.content.res.Resources
import com.vchat.data.local.db.AppDatabase
import com.vchat.data.local.pref.AppPreferenceManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by Fasil on 11/03/23.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppPreferenceManager(application: Application) = AppPreferenceManager(application)

    @Provides
    @Singleton
    fun provideResources(application: Application): Resources = application.applicationContext.resources

    @Singleton
    @Provides
    fun provideRoomDatabase(application: Application): AppDatabase = AppDatabase.getInstance(application)
}