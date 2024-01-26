package com.example.questions

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.questions.todo.data.Question
import com.example.questions.todo.data.local.QuestionDao
import androidx.room.TypeConverters
import com.example.questions.core.utils.Converters

@Database(entities = [Question::class], version = 1)
@TypeConverters(Converters::class)
abstract class QuestionStoreAndroidDatabase : RoomDatabase() {
    abstract fun questionDao(): QuestionDao

    companion object {
        @Volatile
        private var INSTANCE: QuestionStoreAndroidDatabase? = null

        fun getDatabase(context: Context): QuestionStoreAndroidDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    QuestionStoreAndroidDatabase::class.java,
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