package com.example.sello.entity


import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.sello.database.DatabaseConstants.PRODUCT_ID
import com.example.sello.database.DatabaseConstants.PRODUCT_TABLE
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = PRODUCT_TABLE)
data class Product(
    @PrimaryKey @ColumnInfo(name = PRODUCT_ID) val productID: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "image")
    val image: String,
    @ColumnInfo(name = "price")
    val price: Long,
    @ColumnInfo(name = "discount")
    val discount: Double,
    @ColumnInfo(name = "stock")
    val stock: Long,
    val type: String,
) : Parcelable