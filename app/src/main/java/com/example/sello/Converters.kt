package com.example.sello

import androidx.room.TypeConverter
import com.example.sello.entity.OrderStatus
import java.util.*

class Converters {
    @TypeConverter
    fun orderStatusToString(status: OrderStatus): String = status.toString()

    @TypeConverter
    fun stringToOrderStatus(status: String): OrderStatus = OrderStatus.valueOf(status)
}