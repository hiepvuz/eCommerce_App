package com.example.sello.repository

import android.app.Application
import com.example.sello.dao.AppDao
import com.example.sello.database.AppDatabase
import com.example.sello.entity.Product
import io.reactivex.rxjava3.core.Completable

class ProductRepository(application: Application) {

    private val appDao: AppDao

    init {
        val appDatabase: AppDatabase = AppDatabase.getInstance(application)
        appDao = appDatabase.dao
    }

    fun getAllProducts() = appDao.getProducts()

    fun insertProduct(product: Product): Completable {
        return appDao.insertProduct(product)
    }

    fun findProductsByType(key: String) = appDao.findProductByType(key)

    fun findProductByID(id: String) = appDao.findProductByID(id)

    fun findProductByName(name: String) = appDao.findProductByName(name)

    fun updateStock(quantity: Long, id: String): Completable {
        return appDao.updateStock(quantity, id)
    }

}