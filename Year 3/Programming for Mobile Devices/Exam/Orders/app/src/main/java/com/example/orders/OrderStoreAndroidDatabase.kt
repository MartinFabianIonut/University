package com.example.orders

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.orders.todo.data.Order
import com.example.orders.todo.data.local.OrderDao

@Database(entities = [Order::class], version = 1)
abstract class OrderStoreAndroidDatabase : RoomDatabase() {
    abstract fun orderDao(): OrderDao

    companion object {
        @Volatile
        private var INSTANCE: OrderStoreAndroidDatabase? = null

        fun getDatabase(context: Context): OrderStoreAndroidDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    OrderStoreAndroidDatabase::class.java,
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