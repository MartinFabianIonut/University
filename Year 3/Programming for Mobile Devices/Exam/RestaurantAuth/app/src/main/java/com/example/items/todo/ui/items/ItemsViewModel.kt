package com.example.items.todo.ui.items

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.items.ItemStoreAndroid
import com.example.items.core.TAG
import com.example.items.todo.data.Item
import com.example.items.todo.data.ItemRepository
import com.example.items.todo.data.OrderItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ItemsViewModel(private val itemRepository: ItemRepository) : ViewModel() {
    val uiState: Flow<List<Item>> = itemRepository.itemStream
    val orderUiState: Flow<List<OrderItem>> = itemRepository.orderItemStream

    init {
        Log.d(TAG, "init")
        //loadItems()
    }

    fun refreshItems(query : String) {
        viewModelScope.launch {
            itemRepository.refresh(query)
        }
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
