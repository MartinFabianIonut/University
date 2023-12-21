package com.example.bookstoreandroid

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.bookstoreandroid.todo.data.Book
import com.example.bookstoreandroid.todo.data.local.BookDao

@Database(entities = [Book::class], version = 1)
abstract class BookStoreAndroidDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao

    companion object {
        @Volatile
        private var INSTANCE: BookStoreAndroidDatabase? = null

        fun getDatabase(context: Context): BookStoreAndroidDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    BookStoreAndroidDatabase::class.java,
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