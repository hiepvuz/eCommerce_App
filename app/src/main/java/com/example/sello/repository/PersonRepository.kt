package com.example.sello.repository

import android.app.Application
import com.example.sello.dao.AppDao
import com.example.sello.database.AppDatabase
import com.example.sello.entity.Person
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

class PersonRepository(application: Application) {
    private val appDao: AppDao

    init {
        val appDatabase: AppDatabase = AppDatabase.getInstance(application)
        appDao = appDatabase.dao
    }

    fun getPersons() = appDao.getPersons()

    fun insertPerson(person: Person): Completable {
        return appDao.insertPerson(person)
    }

    fun checkPhonePassword(phone: String, password: String) =
        appDao.checkPhonePassword(phone, password)

    fun findPersonByPhone(phone: String) = appDao.findPersonByPhone(phone)
    fun updatePerson(person: Person): Completable {
        return appDao.updatePerson(person)
    }

    fun deletePerson(phone: String): Completable {
        return appDao.deletePerson(phone)
    }

}