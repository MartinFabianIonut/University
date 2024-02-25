package com.example.items.todo.data.remote

import android.annotation.SuppressLint
import android.util.Log
import com.example.items.todo.data.Item
import com.example.items.todo.data.ItemRepository
import com.example.items.todo.data.Product

object ItemApi {

    @SuppressLint("StaticFieldLeak")
    lateinit var itemRepository: ItemRepository

    suspend fun createItem(item: Item): Boolean {
        return try {
            Log.d("Create from WorkManager", "item title: ${item.code}")
            itemRepository.save(item)
            true
        } catch (e: Exception) {
            Log.i("Create failed from WorkManager", e.toString())
            false
        }
    }

    suspend fun updateItem(product: Product): Boolean {
        return try {
            Log.d("Update from WorkManager", "item title: ${product.name}")
            //itemRepository.update(product)
            true
        } catch (e: Exception) {
            Log.i("Update failed from WorkManager", e.toString())
            false
        }
    }
}
