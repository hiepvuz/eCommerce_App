package com.example.sello.entity
import androidx.room.*
import com.example.sello.database.DatabaseConstants.ORDER_ITEM_TABLE
import androidx.room.ForeignKey
import com.example.sello.constants.FireStoreConstants.QUANTITY
import com.example.sello.database.DatabaseConstants.ORDER_ID
import com.example.sello.database.DatabaseConstants.ORDER_ITEM_ID
import com.example.sello.database.DatabaseConstants.PRODUCT_ID
import com.example.sello.database.DatabaseConstants.TIME_ORDER

@Entity(tableName = ORDER_ITEM_TABLE,
        foreignKeys = [androidx.room.ForeignKey(
    entity = Order::class,
    parentColumns = arrayOf(TIME_ORDER),
    childColumns = arrayOf(ORDER_ID),
    onDelete = ForeignKey.CASCADE
)])

data class OrderItem(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ORDER_ITEM_ID) val orderItemID: Int,
    @ColumnInfo(name = ORDER_ID) val orderID: String,
    @ColumnInfo(name = PRODUCT_ID) val productID: String,
    @ColumnInfo(name = QUANTITY) val quantity: Int,
    val price: Long,
    val discount: Double
)

data class OrderWithItems(
    @Embedded val order: Order ,
    @Relation(
        parentColumn = TIME_ORDER,
        entityColumn = ORDER_ID
    )
    val orderItemLists: List<OrderItem>
)
