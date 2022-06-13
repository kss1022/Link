package com.example.link.data.repository

import com.example.link.model.PetModel

interface PetRepository {

    suspend fun savePetData(name: String, isMail: Boolean, type: String, birthday: List<Int>, wight: Float)

    suspend fun  getPetData( userId : String) : PetModel?

    suspend fun checkPetIsExist(userId: String) : Boolean
}