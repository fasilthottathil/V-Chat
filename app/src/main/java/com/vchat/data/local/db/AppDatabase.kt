package com.vchat.data.local.db

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.vchat.data.local.db.dao.ChatsDao
import com.vchat.data.local.db.dao.PostDao
import com.vchat.data.local.db.dao.UserDao
import com.vchat.data.local.db.entity.ChatEntity
import com.vchat.data.local.db.entity.PostEntity
import com.vchat.data.local.db.entity.UserEntity

/**
 * Created by Fasil on 15/03/23.
 */
@Database(entities = [UserEntity::class, PostEntity::class, ChatEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun postDao(): PostDao
    abstract fun chatsDao(): ChatsDao

    companion object {
        fun getInstance(application: Application): AppDatabase {
            return Room.databaseBuilder(
                application,
                AppDatabase::class.java,
                "v-chat.db"
            ).build()
        }
    }
}