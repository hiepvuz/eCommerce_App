package com.example.sello.repository

import android.app.Application
import com.example.sello.dao.AppDao
import com.example.sello.database.AppDatabase
import com.example.sello.entity.Order
import com.example.sello.entity.OrderItem
import io.reactivex.rxjava3.core.Completable

class OrderRepository(application: Application) {
    private val appDao: AppDao

    init {
        val appDatabase: AppDatabase = AppDatabase.getInstance(application)
        appDao = appDatabase.dao
    }

    fun insertOrderItem(orderItem: OrderItem): Completable {
        return appDao.insertOrderItems(orderItem)
    }

    fun insertOrder(order: Order): Completable {
        return appDao.insertOrder(order)
    }
}