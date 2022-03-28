package com.example.sello.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.sello.constants.FireStoreConstants.PHONE
import com.example.sello.constants.FireStoreConstants.QUANTITY
import com.example.sello.database.DatabaseConstants.CART_TABLE
import com.example.sello.database.DatabaseConstants.ORDER_ID
import com.example.sello.database.DatabaseConstants.ORDER_ITEM_TABLE
import com.example.sello.database.DatabaseConstants.ORDER_TABLE
import com.example.sello.database.DatabaseConstants.PERSON_ID
import com.example.sello.database.DatabaseConstants.PERSON_TABLE
import com.example.sello.database.DatabaseConstants.PRODUCT_ID
import com.example.sello.database.DatabaseConstants.PRODUCT_TABLE
import com.example.sello.entity.*
import io.reactivex.rxjava3.core.Completable

@Dao
interface AppDao {
    @Query("SELECT * FROM $PERSON_TABLE ORDER BY name")
    fun getPersons(): List<Person>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPerson(person: Person): Completable

    @Query("SELECT * FROM $PERSON_TABLE WHERE phone = :phone ")
    fun checkPersonLogin(phone: String): Person

    @Query("SELECT * FROM $PERSON_TABLE WHERE $PERSON_ID LIKE :ID")
    fun findPersonByID(ID: String): Person

    @Query("SELECT * FROM $PERSON_TABLE WHERE $PHONE LIKE :key")
    fun findPersonByPhone(key: String): Person

    @Query("DELETE FROM $PERSON_TABLE WHERE $PERSON_ID == :id")
    fun deletePerson(id: String): Completable

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updatePerson(person: Person): Completable

    @Query("SELECT EXISTS(SELECT * FROM $PERSON_TABLE WHERE phone = :phone AND password =:password)")
    fun checkPhonePassword(phone: String, password: String): Boolean

    @Query("SELECT * FROM $ORDER_TABLE")
    fun getOrders(): LiveData<List<Order>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrder(order: Order): Completable

    @Query("SELECT * FROM $ORDER_TABLE WHERE $ORDER_ID = :id ")
    fun findOrderByID(id: Int): Order

//    @Query("SELECT * FROM $ORDER_TABLE WHERE customerID = :id ")
//    fun findOrderByCustomer(id: Int): LiveData<List<Order>>

    @Query("SELECT * FROM $ORDER_TABLE WHERE status = :status ")
    fun findOrderByStatus(status: String): LiveData<List<Order>>

    @Query("DELETE FROM $ORDER_TABLE WHERE $ORDER_ID == :id")
    fun deleteOrder(id: Int): Completable

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateOrder(order: Order): Completable

    @Query("UPDATE $ORDER_TABLE SET status=:status WHERE $ORDER_ID== :id")
    fun updateOrderStatus(id: Int, status: String): Completable

    @Query("SELECT * FROM $PRODUCT_TABLE ORDER BY name")
    fun getProducts(): LiveData<List<Product>>

//    @Query("SELECT * FROM $PERSON_TABLE ORDER BY name")
//    fun getPersons(): List<Person>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(products: List<Product>): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProduct(product: Product) : Completable

    @Query("SELECT * FROM $PRODUCT_TABLE WHERE $PRODUCT_ID == :key")
    fun findProductByID(key: String): Product

    @Query("SELECT * FROM $PRODUCT_TABLE WHERE name LIKE  '%' || :key || '%'")
    fun findProductByName(key: String): LiveData<List<Product>>

    @Query("SELECT * FROM $PRODUCT_TABLE WHERE type LIKE :key")
    fun findProductByType(key: String): LiveData<List<Product>>

    @Query("SELECT * FROM $PRODUCT_TABLE WHERE price > :minPrice")
    fun findProductsByMinPrice(minPrice: Int): LiveData<List<Product>>

    @Query("SELECT * FROM $PRODUCT_TABLE WHERE price < :maxPrice")
    fun findProductsByMaxPrice(maxPrice: Int): LiveData<List<Product>>

    @Query("DELETE FROM $PRODUCT_TABLE WHERE $PRODUCT_ID == :id")
    fun deleteProduct(id: Int): Completable

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateProduct(product: Product): Completable

    @Query("UPDATE $PRODUCT_TABLE SET stock=:quantity WHERE $PRODUCT_ID==:productID")
    fun updateStock(quantity: Long, productID: String): Completable

    @Query("SELECT * FROM $CART_TABLE WHERE $PERSON_ID == :personID")
    fun getAllCart(personID: String): LiveData<List<Cart>>

    @Query("SELECT * FROM $CART_TABLE WHERE $PERSON_ID == :personID")
    fun getListAllCart(personID: String): List<Cart>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addToCart(cart: Cart): Completable

    @Query("DELETE FROM $CART_TABLE WHERE $PERSON_ID == :personID AND $PRODUCT_ID==:productID")
    fun deleteItemFromCart(personID: String, productID: String): Completable

    @Query("UPDATE $CART_TABLE SET $QUANTITY=:quantity WHERE $PERSON_ID== :personID AND $PRODUCT_ID==:productID")
    fun updateCartNumber(quantity: Int, personID: String, productID: String): Completable

    @Query("UPDATE $CART_TABLE SET `checkout`=NOT `checkout` WHERE $PERSON_ID== :personID AND $PRODUCT_ID==:productID")
    fun updateCartCheckout(personID: String, productID: String)

    @Query("UPDATE $CART_TABLE SET `checkout`= 1 WHERE $PERSON_ID== :personID")
    fun updateAllCheckout(personID: String): Completable

    @Query("UPDATE $CART_TABLE SET `checkout`= 0 WHERE $PERSON_ID== :personID")
    fun updateAllNotCheckout(personID: String): Completable

    @Query("SELECT * FROM $CART_TABLE WHERE $PERSON_ID == :id AND checkout")
    fun searchCartCheckOut(id: String): List<Cart>

    @Query("DELETE FROM $CART_TABLE WHERE $PERSON_ID == :id AND checkout")
    fun checkoutCart(id: String): Completable

    @Query("SELECT * FROM $CART_TABLE WHERE $PERSON_ID == :id AND checkout")
    fun searchListCartCheckout(id: String): LiveData<List<Cart>>

    @Query("SELECT * FROM $ORDER_ITEM_TABLE")
    fun getOrderItems(): LiveData<List<OrderItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrderItems(orderItem: OrderItem): Completable

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateOrderItem(orderItem: OrderItem): Completable

    @Query("SELECT * FROM $ORDER_ITEM_TABLE WHERE $ORDER_ID = :id ")
    fun findOrderItemByOrder(id: String): LiveData<List<OrderItem>>

    @Query("SELECT * FROM $ORDER_TABLE join $ORDER_ITEM_TABLE ON $ORDER_TABLE.$ORDER_ID = $ORDER_ITEM_TABLE.$ORDER_ID")
    fun findOrderAndItems(): LiveData<List<OrderWithItems>>

    @Query("SELECT * FROM $ORDER_TABLE join $ORDER_ITEM_TABLE ON $ORDER_TABLE.$ORDER_ID = $ORDER_ITEM_TABLE.$ORDER_ID WHERE $ORDER_TABLE.$ORDER_ID = :id ")
    fun findOrderAndItemsByID(id: String): LiveData<List<OrderWithItems>>
}