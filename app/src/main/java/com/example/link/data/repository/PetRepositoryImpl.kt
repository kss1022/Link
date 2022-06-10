package com.example.link.data.repository

import com.example.link.data.api.FireStoreApi
import com.example.link.data.entity.PetEntity
import com.example.link.model.PetModel
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

        fireStoreApi.savePetData(PetEntity(
            name = name,
            mail = isMail,
            type =type,
            birthDay = birthday,
            weight = wight,
        ) ,
            userId!!
        )
    }

    override suspend fun getPetData(userId: String): PetModel? = withContext(ioDispatcher){
        val entity = fireStoreApi.getPetData(userId)

        entity?.let {
            PetModel(
                name = it.name!!,
                isMail = it.mail!!,
                type = it.type!!,
                birthDay = it.birthDay!!,
                weight = it.weight!!,
            )
        }
    }

    override suspend fun checkPetIsExist(userId: String): Boolean = withContext(ioDispatcher){
        fireStoreApi.checkPetIsExist(userId)
    }
}