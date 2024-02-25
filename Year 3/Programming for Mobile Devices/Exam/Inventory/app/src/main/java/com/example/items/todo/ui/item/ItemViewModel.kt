package com.example.items.todo.ui.item

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.items.ItemStoreAndroid
import com.example.items.core.Result
import com.example.items.core.TAG
import com.example.items.todo.data.Item
import com.example.items.todo.data.ItemRepository
import kotlinx.coroutines.launch


data class ItemUiState(
    val itemId: Int? = null,
    val item: Item = Item(),
    var loadResult: Result<Item>? = null,
    var submitResult: Result<Item>? = null,
)

class ItemViewModel(private val itemId: Int?, private val itemRepository: ItemRepository) :
    ViewModel() {

    var uiState: ItemUiState by mutableStateOf(ItemUiState(loadResult = Result.Loading))
        private set

    init {
        Log.d(TAG, "init")
        if (itemId != null) {
            loadItem()
        } else {
            uiState = uiState.copy(loadResult = Result.Success(Item()))
        }
    }

    private fun loadItem() {
        viewModelScope.launch {
            itemRepository.itemStream.collect { items ->
                Log.d(TAG, "Collecting items")
                if (uiState.loadResult !is Result.Loading) {
                    return@collect
                }
                Log.d(TAG, "Collecting items - loadResult is loading, attempting to find item with id: $itemId")
                val item = items.find { it.code == itemId } ?: Item()
                Log.d(TAG, "Collecting items - item: $item")
                uiState = uiState.copy(item = item, loadResult = Result.Success(item))
            }
        }
    }

    fun saveOrUpdateItem(
        code: Int,
        quantity: Int,
    ) {
        viewModelScope.launch {
            try {
                uiState = uiState.copy(submitResult = Result.Loading)

                val item = uiState.item.copy(
                    code = code,
                    quantity = quantity
                )

                val savedItem: Item = itemRepository.save(item)

                uiState = uiState.copy(submitResult = Result.Success(savedItem))
            } catch (e: Exception) {
                uiState = uiState.copy(submitResult = Result.Error(e))
            }
        }
    }

    fun uploadItems() {
        viewModelScope.launch {

        }
    }


    companion object {
        fun Factory(itemId: Int?): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as ItemStoreAndroid)
                ItemViewModel(itemId, app.container.itemRepository)
            }
        }
    }
}
