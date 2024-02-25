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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch


data class ItemUiState(
    val error: Boolean? = false,
)

class ItemsViewModel(private val itemRepository: ItemRepository) : ViewModel() {
    val uiState: Flow<List<Item>> = itemRepository.itemStream




    init {
        Log.d(TAG, "init")
        //loadItems()
    }

    fun loadItems(onResult : (Boolean) -> Unit) {
        Log.d(TAG, "loadItems...")
        viewModelScope.launch {
            try{
                itemRepository.refresh()
                onResult(true)
            } catch (e: Exception) {
                Log.w(TAG, "refresh failed", e)
                onResult(false)

            }
        }
    }

    fun updateItem(item : Item, onResult: (Boolean) -> Unit) {
        Log.d(TAG, "updateItem")
        viewModelScope.launch {
            try{
                itemRepository.update(item)
                onResult(true)
            } catch (e: Exception) {
                Log.w(TAG, "updateItem failed", e)
                onResult(false)
            }
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
