package com.example.items.todo.data.remote

import android.annotation.SuppressLint
import android.util.Log
import com.example.items.todo.data.Item
import com.example.items.todo.data.ItemRepository

object ItemApi {

    @SuppressLint("StaticFieldLeak")
    lateinit var itemRepository: ItemRepository

    suspend fun createItem(item: Item): Boolean {
        return try {
            Log.d("Create from WorkManager", "item title: ${item.number}")
            itemRepository.save(item)
            true
        } catch (e: Exception) {
            Log.i("Create failed from WorkManager", e.toString())
            false
        }
    }

    suspend fun updateItem(item: Item): Boolean {
        return try {
            Log.d("Update from WorkManager", "item title: ${item.number}")
            itemRepository.update(item)
            true
        } catch (e: Exception) {
            Log.i("Update failed from WorkManager", e.toString())
            false
        }
    }
}
