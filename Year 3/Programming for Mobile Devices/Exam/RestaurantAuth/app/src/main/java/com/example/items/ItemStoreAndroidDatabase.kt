package com.example.items

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.items.todo.data.Item
import androidx.room.TypeConverters
import com.example.items.core.utils.Converters
import com.example.items.todo.data.OrderItem
import com.example.items.todo.data.local.ItemDao
import com.example.items.todo.data.local.OrderItemDao

@Database(entities = [Item::class, OrderItem::class], version = 1)
@TypeConverters(Converters::class)
abstract class ItemStoreAndroidDatabase : RoomDatabase() {
    abstract fun itemDao(): ItemDao

    abstract fun orderItemDao(): OrderItemDao

    companion object {
        @Volatile
        private var INSTANCE: ItemStoreAndroidDatabase? = null

        fun getDatabase(context: Context): ItemStoreAndroidDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    ItemStoreAndroidDatabase::class.java,
                    "restaurant_database2"
                )
                    .setJournalMode(JournalMode.WRITE_AHEAD_LOGGING)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}