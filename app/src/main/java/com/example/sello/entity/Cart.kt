package com.example.sello.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import com.example.sello.constants.FireStoreConstants.QUANTITY
import com.example.sello.database.DatabaseConstants.CART_TABLE
import com.example.sello.database.DatabaseConstants.PERSON_ID
import com.example.sello.database.DatabaseConstants.PRODUCT_ID
import kotlinx.parcelize.Parcelize

@Entity(tableName = CART_TABLE, primaryKeys = [PERSON_ID, PRODUCT_ID],
    foreignKeys = [androidx.room.ForeignKey(
        entity = Person::class,
        parentColumns = arrayOf(PERSON_ID),
        childColumns = arrayOf(PERSON_ID),
        onDelete = ForeignKey.CASCADE
    )])
@Parcelize
data class Cart (
    @ColumnInfo(name = PERSON_ID) val personID: String,
    @ColumnInfo(name = PRODUCT_ID) val productID: String,
    @ColumnInfo(name = QUANTITY) var quantity: Int,
    var checkout : Boolean
):Parcelable
