package com.example.sello.entity
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.sello.database.DatabaseConstants.ORDER_ID
import com.example.sello.database.DatabaseConstants.ORDER_TABLE
import com.example.sello.database.DatabaseConstants.TIME_ORDER
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = ORDER_TABLE,
    indices = [Index(value = [TIME_ORDER], unique = true)])
data class Order(
    @ColumnInfo(name = ORDER_ID) val orderID: String,
    @PrimaryKey @ColumnInfo(name = TIME_ORDER) val timeOrder: String,
    val status: OrderStatus
):Parcelable

enum class OrderStatus {
    Pending, Confirmed, Shipped
}