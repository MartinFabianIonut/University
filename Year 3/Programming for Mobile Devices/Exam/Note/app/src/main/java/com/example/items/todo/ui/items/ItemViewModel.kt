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
import kotlinx.coroutines.delay
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

    suspend fun isInList(id: Int) : Boolean {
        return itemRepository.find(id)
    }

    fun getNotesCached(ifModifiedSince: String, page: Int, onResult : (Boolean) -> Unit) {
        viewModelScope.launch {
            itemRepository.getNotesCached(ifModifiedSince, page) {
                isSuccess ->
                    if (isSuccess) {
                        onResult(true)
                    } else {
                        onResult(false)
                    }
            }
        }
    }

    fun reopenWsConnection() {
        viewModelScope.launch {
            //itemRepository.closeWsClient()
            itemRepository.openWsClient()
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
