package com.example.orders.core.utils

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.orders.OrderStoreAndroidDatabase
import com.example.orders.todo.data.remote.OrderApi

class MyWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val orderDao = OrderStoreAndroidDatabase.getDatabase(applicationContext).orderDao()
        val dirtyOrders = orderDao.getDirtyOrders()
        dirtyOrders.forEach {dirtyOrder ->
            Log.d("MyWorker do work", dirtyOrder.toString())
            val newOrder = dirtyOrder.copy(dirty = false)
            if (dirtyOrder.code.toInt() > 0) {
                OrderApi.updateOrder(newOrder)
            } else {
                OrderApi.createOrder(newOrder)
                Log.d(
                    "Update from WorkManager",
                    newOrder.toString()
                )
                Thread.sleep(1000)
                orderDao.deleteOrder(dirtyOrder)
                Log.d("Delete from WorkManager",
                    "SUCCESS : $dirtyOrder deleted"
                )
            }
        }
        if (dirtyOrders.isNotEmpty()){
            showSimpleNotificationWithTapAction(
                context,
                "Orders Channel",
                0,
                "Synchronization complete",
                "Your list of orders has been synchronized with the server."
            )
        }
        return Result.success()
    }
}