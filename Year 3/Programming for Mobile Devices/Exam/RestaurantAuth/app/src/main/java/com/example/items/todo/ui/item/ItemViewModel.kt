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
import com.example.items.todo.data.OrderItem
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
                Log.d(
                    TAG,
                    "Collecting items - loadResult is loading, attempting to find item with id: $itemId"
                )
                val item = items.find { it.code == itemId } ?: Item()
                Log.d(TAG, "Collecting items - item: $item")
                uiState = uiState.copy(item = item, loadResult = Result.Success(item))
            }
        }
    }

    fun order(item: Item, quantity: Int, onResult: (Boolean) -> Unit) {
        Log.d(TAG, "O intra in order din itemViewModel")
        var orderItem = OrderItem(code = item.code, quantity = quantity, free = false)
        viewModelScope.launch {
            try{
                itemRepository.order(orderItem)
                onResult(true)
            } catch (e: Exception) {
                Log.d(TAG, "Order failed")
                onResult(false)
            }
            Log.d(TAG, "Am primit: ")
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
