package com.example.items.todo.ui.items

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
import com.example.items.core.TAG
import com.example.items.todo.data.Item
import com.example.items.todo.data.ItemRepository
import com.example.items.todo.data.Product
import com.example.items.todo.data.remote.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

data class ProductState(
    val currentPage: Int = 1,
    val numberOfPages: Int = 0,
    val isDownloading: Boolean = false,
    val isDownloaded: Boolean = false,
)

data class UploadState(
    val isUploading: Boolean = false,
    val isUploaded: Boolean = false,
    val currentItemIndex: Int = 0,
    val totalItems: Int = 0,
)

class ItemsViewModel(private val itemRepository: ItemRepository) : ViewModel() {
    val repository = itemRepository
    val uiState: Flow<List<Product>> = repository.productStream
    val itemState: Flow<List<Item>> = repository.itemStream
    var productState: ProductState by mutableStateOf(ProductState())
    var uploadState: UploadState by mutableStateOf(UploadState())

    init {
        Log.d(TAG, "init")
        startDownload()
    }

    private fun loadItems(page: Int) {
        Log.d(TAG, "loadItems...")
        viewModelScope.launch {
            repository.refresh(page)
        }
    }

    fun startDownload() {
        Log.d(TAG, "startDownload...")
        viewModelScope.launch {
            val response = repository.refresh(1)
            var response1: Response? = null
            if (response == null) {
                repository.downloadState = repository.downloadState.copy(show = true)
            } else {
                productState = productState.copy(
                    currentPage = response.page,
                    numberOfPages = response.total,
                    isDownloading = true,
                    isDownloaded = false,
                )
                while (productState.currentPage < productState.numberOfPages) {
                    response1 = repository.refresh(productState.currentPage + 1)
                    if (response1 == null) {
                        repository.downloadState = repository.downloadState.copy(show = true)
                        break
                    }
                    productState = productState.copy(
                        currentPage = response1.page,
                        numberOfPages = response1.total,
                        isDownloading = true,
                        isDownloaded = false,
                    )
                }
                if (response1 != null) {
                    productState = productState.copy(
                        isDownloading = false,
                        isDownloaded = true,
                    )
                    repository.downloadState = repository.downloadState.copy(show = false)
                }
            }
        }
    }

    suspend fun search(query: String): List<Product> {
        Log.d(TAG, "searchItems...")
        return repository.search(query)
    }

    fun uploadAllItems() {
        Log.d(TAG, "updateAllItems...")
        viewModelScope.launch {
            val dirtyItems = repository.getItemsToUpdate()
            uploadState = uploadState.copy(
                isUploading = true,
                isUploaded = false,
                currentItemIndex = 0,
                totalItems = dirtyItems.size,
            )
            for (item in dirtyItems) {
                repository.saveToServer(item)
                uploadState = uploadState.copy(
                    isUploading = true,
                    isUploaded = false,
                    currentItemIndex = uploadState.currentItemIndex + 1,
                    totalItems = dirtyItems.size,
                )
            }
            uploadState = uploadState.copy(
                isUploading = false,
                isUploaded = true,
                currentItemIndex = 0,
                totalItems = 0,
            )
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
