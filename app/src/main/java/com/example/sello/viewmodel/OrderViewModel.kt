package com.example.sello.viewmodel

import android.app.Application
import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.sello.constants.FireStoreConstants
import com.example.sello.entity.Order
import com.example.sello.entity.OrderItem
import com.example.sello.repository.*
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OrderViewModel(application: Application) : AndroidViewModel(application) {

    private val orderRepository = OrderRepository(application)
    private val fireStoreRepository = FireStoreRepository(application)



    fun insertOrderItem(orderItem: OrderItem): Disposable? = orderRepository.insertOrderItem(orderItem).subscribeOn(
        Schedulers.computation())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnError {
            Log.w(
                ContentValues.TAG,
                FireStoreConstants.MESS_ERROR)
        }
        .subscribe()

    fun insertOrder(order: Order): Disposable? = orderRepository.insertOrder(order).subscribeOn(Schedulers.computation())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnError{
            Log.w(
                ContentValues.TAG,
                FireStoreConstants.MESS_ERROR)
        }
        .subscribe()


    fun addOrderItemToFireStore(orderItem: OrderItem, personId: String) = viewModelScope.launch(Dispatchers.IO){
         fireStoreRepository.addOrderItemToFireStore(orderItem,personId)
    }

}