package com.example.sello.viewmodel

import android.app.Application
import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.sello.constants.FireStoreConstants
import com.example.sello.entity.Cart
import com.example.sello.repository.CartRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CartViewModel(application: Application) : AndroidViewModel(application) {
    private val cartRepository = CartRepository(application)


    fun addToCart(cart: Cart): Disposable =
        cartRepository.addToCart(cart).subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()

    fun getAllCart(personID: String) = cartRepository.getAllCart(personID)

    fun getListAllCart(personID: String) = cartRepository.getListAllCart(personID)

    fun searchCartCheckOut(personID: String) = cartRepository.searchCartCheckOut(personID)

    fun deleteCart(personID: String, productID: String): Disposable? =
        cartRepository.deleteCart(personID, productID).subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError{
                Log.w(
                    ContentValues.TAG,
                    FireStoreConstants.MESS_ERROR)
            }
            .subscribe()


    fun searchListCartCheckout(personID: String) = cartRepository.searchListCartCheckout(personID)

    fun updateCartNumber(quantity: Int, personID: String, productID: String): Disposable? =
        cartRepository.updateCartNumber(quantity, personID, productID).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError{
                Log.w(
                    ContentValues.TAG,
                    FireStoreConstants.MESS_ERROR)
            }
            .subscribe()


    fun checkoutCart(id: String): Disposable? =
        cartRepository.checkoutCart(id).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError{
                Log.w(
                    ContentValues.TAG,
                    FireStoreConstants.MESS_ERROR)
            }
            .subscribe()


    fun updateCartCheckout(personID: String, productID: String) = viewModelScope.launch {
        cartRepository.updateCartCheckout(personID, productID)
    }

    fun updateAllCheckout(personID: String) = viewModelScope.launch(Dispatchers.IO) {
        cartRepository.updateAllCheckout(personID)
    }

    fun updateAllNotCheckout(personID: String) = viewModelScope.launch(Dispatchers.IO){
        cartRepository.updateAllCheckout(personID)
    }
}