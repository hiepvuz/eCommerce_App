package com.example.sello.repository

import android.app.Application
import android.content.ContentValues
import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.example.sello.constants.FireStoreConstants.ADDRESS
import com.example.sello.constants.FireStoreConstants.BALANCE
import com.example.sello.constants.FireStoreConstants.DESCRIPTION
import com.example.sello.constants.FireStoreConstants.DISCOUNT
import com.example.sello.constants.FireStoreConstants.EMAIL
import com.example.sello.constants.FireStoreConstants.GENDER
import com.example.sello.constants.FireStoreConstants.IMAGE
import com.example.sello.constants.FireStoreConstants.MESS_ERROR
import com.example.sello.constants.FireStoreConstants.MESS_EXITED
import com.example.sello.constants.FireStoreConstants.MESS_SUCCESS
import com.example.sello.constants.FireStoreConstants.NAME
import com.example.sello.constants.FireStoreConstants.ORDERS
import com.example.sello.constants.FireStoreConstants.ORDER_ID
import com.example.sello.constants.FireStoreConstants.PASSWORD
import com.example.sello.constants.FireStoreConstants.PHONE
import com.example.sello.constants.FireStoreConstants.PRICE
import com.example.sello.constants.FireStoreConstants.PRODUCTS
import com.example.sello.constants.FireStoreConstants.PRODUCT_ID
import com.example.sello.constants.FireStoreConstants.QUANTITY
import com.example.sello.constants.FireStoreConstants.STOCK
import com.example.sello.constants.FireStoreConstants.TOTAL_PRICE
import com.example.sello.constants.FireStoreConstants.TYPE
import com.example.sello.constants.FireStoreConstants.USERS
import com.example.sello.entity.OrderItem
import com.example.sello.entity.Person
import com.example.sello.entity.Product
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeout
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume

class FireStoreRepository(application: Application) {

    private val db = Firebase.firestore

    fun addFireStore(person: Person){
        val user = HashMap<String, Any>()
        user[NAME] = person.name
        user[EMAIL] = person.email
        user[PHONE] = person.phone
        user[ADDRESS] = person.address
        user[PASSWORD] = person.password
        user[GENDER] = person.gender
        user[BALANCE] = 0
        var checkExist = false
        db.collection(USERS)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    if (document.id == person.phone) {
                        checkExist = true
                        break
                    }
                }
                if (!checkExist) {
                    db.collection(USERS).document(person.phone)
                        .set(user)
                        .addOnSuccessListener {
                            Log.d(ContentValues.TAG, MESS_SUCCESS)
                        }

                        .addOnFailureListener { e ->
                            Log.w(ContentValues.TAG, MESS_ERROR, e)
                        }
                } else {
                    Log.d(ContentValues.TAG, MESS_EXITED)
                }

            }
    }

    suspend fun getDataFromFireStore(): ArrayList<Person> {
        return suspendCoroutineWithTimeout(10000L) { listPersons ->
            val list = ArrayList<Person>()
            db.collection(USERS)
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        Log.d(ContentValues.TAG, "${document.id} => ${document.data}")
                        val person = Person(document.id,
                            document.data[NAME].toString(),
                            document.data[EMAIL].toString(),
                            document.data[PHONE].toString(),
                            document.data[PASSWORD].toString(),
                            document.data[ADDRESS].toString(),
                            document.data[GENDER].toString(),
                            0
                        )
                        list.add(person)
                    }
                    listPersons.resume(list)
                }
        }
    }

    suspend fun updateDataToFireStore(person: Person) {
        return suspendCoroutineWithTimeout(10000L) {
            val updates = hashMapOf<String, Any>(
                NAME to person.name,
                EMAIL to person.email,
                ADDRESS to person.address,
                GENDER to person.gender,
            )
            db.collection(USERS)
                .document(person.phone)
                .update(updates)
                .addOnSuccessListener {
                    Log.d(ContentValues.TAG,
                        MESS_SUCCESS)
                }
                .addOnFailureListener { e ->
                    Log.w(ContentValues.TAG,
                        MESS_ERROR,
                        e)
                }
        }
    }

    suspend fun updateProductToFireStore(product: Product,stock :Long) {
        return suspendCoroutineWithTimeout(10000L) {
            val updates = hashMapOf<String, Any>(
                STOCK to stock,
            )
            db.collection(PRODUCTS)
                .document(product.productID)
                .update(updates)
                .addOnSuccessListener {
                    Log.d(ContentValues.TAG,
                        MESS_SUCCESS)
                }
                .addOnFailureListener { e ->
                    Log.w(ContentValues.TAG,
                        MESS_ERROR,
                        e)
                }
        }
    }


    fun deletePersonFromFireStore(person: Person) {
        db.collection(USERS)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    if (document.id == person.phone) {
                        db.collection(USERS).document(person.phone)
                            .delete()
                            .addOnSuccessListener {
                                Log.d(ContentValues.TAG,
                                    MESS_SUCCESS)
                            }
                            .addOnFailureListener { e ->
                                Log.w(ContentValues.TAG,
                                    MESS_ERROR,
                                    e)
                            }
                    }
                }
            }
    }

    suspend fun getProductFromFireStore(): ArrayList<Product> {
        val list = arrayListOf<Product>()
        return suspendCoroutineWithTimeout(10000L) { listP ->
            db.collection(PRODUCTS)
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        Log.d(ContentValues.TAG, "${document.id} => ${document.data}")
                        val product = Product(document.id,
                            document.data[NAME].toString(),
                            document.data[DESCRIPTION].toString(),
                            document.data[IMAGE].toString(),
                            document.data[PRICE] as Long,
                            document.data[DISCOUNT] as Double,
                            document.data[STOCK] as Long,
                            document.data[TYPE].toString()
                        )
                        list.add(product)
                    }
                    listP.resume(list)
                }

        }
    }

    fun addOrderItemToFireStore(orderItem: OrderItem, personId :String) {
        val order = HashMap<String, Any>()
        order[ORDER_ID] = orderItem.orderID
        order[PRODUCT_ID] = orderItem.productID
        order[QUANTITY] = orderItem.quantity
        order[TOTAL_PRICE] = orderItem.price
        order[DISCOUNT] = orderItem.discount

        db.collection(ORDERS).document(personId)
            .collection(orderItem.orderID)
            .add(order)
            .addOnSuccessListener {
                Log.d(ContentValues.TAG, MESS_SUCCESS)
            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, MESS_ERROR, e)
            }

    }


    private suspend inline fun <T> suspendCoroutineWithTimeout(
        timeout: Long,
        crossinline block: (Continuation<T>) -> Unit,
    ) = withTimeout(timeout) {
        suspendCancellableCoroutine(block = block)
    }
}