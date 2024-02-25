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
                val item = items.find { it.id == itemId } ?: Item()
                Log.d(TAG, "Collecting items - item: $item")
                uiState = uiState.copy(item = item, loadResult = Result.Success(item))
            }
        }
    }

    fun deleteItem(id: Int, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                uiState = uiState.copy(submitResult = Result.Loading)
                itemRepository.delete(id)
                uiState = uiState.copy(submitResult = Result.Success(uiState.item))
                onResult(true)
            } catch (e: Exception) {
                Log.w(TAG, "deleteItem failed", e)
                uiState = uiState.copy(submitResult = Result.Error(e))
                onResult(false)
            }
        }
    }

//    fun saveOrUpdateItem(
//        id: Int,
//        text: String,
//        options: List<Int>,
//        indexCorrectOption: Int,
//        selectedIndex: Int
//    ) {
//        viewModelScope.launch {
//            try {
//                uiState = uiState.copy(submitResult = Result.Loading)
//
//                // Convert publicationDate to a valid date format if needed
////                val formattedDate = DateUtils.parseDDMMYYYY(publicationDate)
////                if (formattedDate == null) {
////                    uiState = uiState.copy(submitResult = Result.Error(Exception("Invalid date format")))
////                    return@launch
////                }
////                val formattedDateStr = formattedDate.toString()
//
//                val item = uiState.item.copy(
//                    id = id,
//                    text = text,
//                    options = options,
//                    indexCorrectOption = indexCorrectOption,
//                    selectedIndex = selectedIndex
//                )
//
//                val savedItem: Item = if (itemId == null) {
//                    itemRepository.save(item)
//                } else {
//                    itemRepository.update(item)
//                }
//
//                uiState = uiState.copy(submitResult = Result.Success(savedItem))
//            } catch (e: Exception) {
//                uiState = uiState.copy(submitResult = Result.Error(e))
//            }
//        }
//    }

//    fun updateLocally(
//        id: Int,
//        text: String,
//        options: List<Int>,
//        indexCorrectOption: Int,
//        selectedIndex: Int
//    ) {
//        viewModelScope.launch {
//            try {
//                uiState = uiState.copy(submitResult = Result.Loading)
//
////                val item = uiState.item.copy(
////                    id = id,
////                    text = text,
////                    options = options,
////                    indexCorrectOption = indexCorrectOption,
////                    selectedIndex = selectedIndex
////                )
//
//                itemRepository.handleItemUpdated(item)
//
//                uiState = uiState.copy(submitResult = Result.Success(item))
//            } catch (e: Exception) {
//                uiState = uiState.copy(submitResult = Result.Error(e))
//            }
//        }
//    }


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
