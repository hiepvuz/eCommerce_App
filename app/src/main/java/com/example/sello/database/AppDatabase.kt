package com.example.sello.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.sello.*
import com.example.sello.dao.AppDao
import com.example.sello.database.DatabaseConstants.STORE_DB
import com.example.sello.entity.*


@Database(entities = [Order::class, Person::class, Product::class, OrderItem::class, Cart::class], version = 4 , exportSchema = false)

@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract val dao: AppDao

    companion object {

        @Volatile private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context,
                AppDatabase::class.java, STORE_DB
            ).fallbackToDestructiveMigration().allowMainThreadQueries().build()
        }
    }
}