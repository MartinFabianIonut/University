package com.example.chat.todo.ui.items

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.chat.ItemStoreAndroid
import com.example.chat.core.TAG
import com.example.chat.todo.data.Item
import com.example.chat.todo.data.ItemRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ItemsViewModel(private val itemRepository: ItemRepository) : ViewModel() {
    val uiState: Flow<List<Item>> = itemRepository.itemStream

    init {
        Log.d(TAG, "init")
        loadItems()
    }

    private fun loadItems() {
        Log.d(TAG, "loadItems...")
        viewModelScope.launch {
            itemRepository.refresh()
        }
    }

    fun sendMessage(message: String, room: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                itemRepository.sendMessage(message, room)
                Thread.sleep(1000)
                Log.d(TAG, "sendMessage succeeded")
                onResult(true)
            } catch (e: Exception) {
                Log.e(TAG, "sendMessage failed", e)
                onResult(false)
            }
        }

    }
    fun markMessageDirty(text : String, roomId : String){
        viewModelScope.launch { itemRepository.addDirtyMessage(text,roomId) }
    }

    fun markMessageClean(text : String, roomId : String){
        viewModelScope.launch { itemRepository.removeDirtyMessage(text,roomId) }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as ItemStoreAndroid)
                ItemsViewModel(app.container.itemRepository)
            }
        }
    }
}
