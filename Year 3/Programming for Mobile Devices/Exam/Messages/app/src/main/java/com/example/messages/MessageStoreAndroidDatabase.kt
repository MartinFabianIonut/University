package com.example.messages

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.messages.todo.data.Message
import com.example.messages.todo.data.local.MessageDao

@Database(entities = [Message::class], version = 1)
abstract class MessageStoreAndroidDatabase : RoomDatabase() {
    abstract fun messageDao(): MessageDao

    companion object {
        @Volatile
        private var INSTANCE: MessageStoreAndroidDatabase? = null

        fun getDatabase(context: Context): MessageStoreAndroidDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    MessageStoreAndroidDatabase::class.java,
                    "app_database"
                )
                    .setJournalMode(JournalMode.WRITE_AHEAD_LOGGING)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}