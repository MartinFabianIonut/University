package com.example.items

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.items.core.utils.Converters
import com.example.items.todo.data.Item
import com.example.items.todo.data.Product
import com.example.items.todo.data.local.ItemDao
import com.example.items.todo.data.local.ProductDao

@Database(entities = [Product::class, Item::class], version = 1)
@TypeConverters(Converters::class)
abstract class ItemStoreAndroidDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun itemDao(): ItemDao

    companion object {
        @Volatile
        private var INSTANCE: ItemStoreAndroidDatabase? = null

        fun getDatabase(context: Context): ItemStoreAndroidDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    ItemStoreAndroidDatabase::class.java,
                    "app_database_4"
                )
                    .setJournalMode(JournalMode.WRITE_AHEAD_LOGGING)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}