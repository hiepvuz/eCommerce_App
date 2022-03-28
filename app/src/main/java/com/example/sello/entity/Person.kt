package com.example.sello.entity
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.sello.database.DatabaseConstants.PERSON_ID
import com.example.sello.database.DatabaseConstants.PERSON_TABLE
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = PERSON_TABLE)
data class Person(
    @PrimaryKey @ColumnInfo(name = PERSON_ID) val personId: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "email")
    val email: String,
    @ColumnInfo(name = "phone")
    val phone: String,
    @ColumnInfo(name = "password")
    val password: String,
    @ColumnInfo(name = "address")
    val address: String,
    @ColumnInfo(name = "gender")
    val gender: String,
    @ColumnInfo(name = "balance")
    val balance: Int
):Parcelable