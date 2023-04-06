package com.vchat.di

import android.content.res.Resources
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.vchat.data.local.db.AppDatabase
import com.vchat.data.local.pref.AppPreferenceManager
import com.vchat.data.repository.ChatsRepositoryImpl
import com.vchat.data.repository.PostRepositoryImpl
import com.vchat.data.repository.UserRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Created by Fasil on 11/03/23.
 */
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideUserRepository(
        firebaseAuth: FirebaseAuth,
        firebaseFirestore: FirebaseFirestore,
        appDatabase: AppDatabase,
        resources: Resources
    ): UserRepositoryImpl = UserRepositoryImpl(firebaseAuth, firebaseFirestore, appDatabase, resources)

    @Provides
    fun provideChatsRepository(
        appPreferenceManager: AppPreferenceManager,
        firebaseFirestore: FirebaseFirestore,
        appDatabase: AppDatabase,
        resources: Resources
    ): ChatsRepositoryImpl = ChatsRepositoryImpl(appPreferenceManager, appDatabase, firebaseFirestore, resources)

    @Provides
    fun providePotRepository(
        firebaseFirestore: FirebaseFirestore,
        firebaseStorage: FirebaseStorage,
        appDatabase: AppDatabase,
        resources: Resources
    ): PostRepositoryImpl = PostRepositoryImpl(firebaseFirestore, firebaseStorage, appDatabase, resources)

}