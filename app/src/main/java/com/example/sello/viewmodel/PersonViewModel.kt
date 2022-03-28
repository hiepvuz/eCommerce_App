package com.example.sello.viewmodel

import android.app.Application
import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.AndroidViewModel

import androidx.lifecycle.viewModelScope
import com.example.sello.constants.FireStoreConstants
import com.example.sello.entity.Person
import com.example.sello.repository.FireStoreRepository
import com.example.sello.repository.PersonRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PersonViewModel(application: Application) : AndroidViewModel(application) {
    private val personRepository: PersonRepository = PersonRepository(application)
    private val fireStoreRepository = FireStoreRepository(application)

    fun getPerson() = personRepository.getPersons()

    fun insertPerson(person: Person) {
        personRepository.insertPerson(person).subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {
                Log.w(
                    ContentValues.TAG,
                    FireStoreConstants.MESS_ERROR
                )
            }.subscribe()
    }

    fun checkPhonePass(phone: String, pass: String) =
        personRepository.checkPhonePassword(phone, pass)

    fun findPersonByPhone(phone: String) = personRepository.findPersonByPhone(phone)

    fun updatePerson(person: Person): Disposable? =
        personRepository.updatePerson(person).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {
                Log.w(
                    ContentValues.TAG,
                    FireStoreConstants.MESS_ERROR
                )
            }.subscribe()

    fun deletePerson(phone: String): Disposable? =
        personRepository.deletePerson(phone).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {
                Log.w(
                    ContentValues.TAG,
                    FireStoreConstants.MESS_ERROR
                )
            }.subscribe()

    fun addFireStore(person: Person) = fireStoreRepository.addFireStore(person)

    fun getDataFromFireStore() = viewModelScope.launch() {
        val list = fireStoreRepository.getDataFromFireStore()
        list.forEach { person ->
            insertPerson(person)
        }
    }


    fun updatePersonToFireStore(person: Person) = viewModelScope.launch(Dispatchers.IO) {
        fireStoreRepository.updateDataToFireStore(person)
    }


    fun deletePersonFromFireStore(person: Person) = viewModelScope.launch(Dispatchers.IO) {
        fireStoreRepository.deletePersonFromFireStore(person)
    }
}
