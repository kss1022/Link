package com.example.link.data.repository

import com.example.link.data.api.FireStoreApi
import com.example.link.data.entity.PatEntity
import com.example.yourchoice.data.preference.PreferenceManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PetRepositoryImpl @Inject constructor(
    private val ioDispatcher: CoroutineDispatcher,
    private val fireStoreApi: FireStoreApi,
    private val preferenceManager: PreferenceManager
): PetRepository{


    override suspend fun savePetData(
        name: String,
        isMail: Boolean,
        type: String,
        birthday: List<Int>,
        wight: Int
    ) = withContext(ioDispatcher){

        val userId = preferenceManager.getUserId()

        fireStoreApi.savePetData(PatEntity(
            name = name,
            isMail = isMail,
            type =type,
            birthDay = birthday,
            weight = wight,
        ) ,
            userId!!
        )
    }
}