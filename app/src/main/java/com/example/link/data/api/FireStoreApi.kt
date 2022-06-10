package com.example.link.data.api

import com.example.link.data.entity.PetEntity
import com.example.link.model.User

interface FireStoreApi {

    suspend fun saveUserId(id: String, email: String)

    suspend fun saveUserNameWithProfileNum(id :String, name : String, num : Int)

    suspend fun savePetData(petEntity: PetEntity, userId: String)

    suspend fun getPetData(userId: String) : PetEntity?

    suspend fun checkUserIsExist(userId: String) : Boolean

    suspend fun checkPetIsExist(userId: String) : Boolean

    suspend fun getUserProfileNum(userId: String) : Int

    suspend fun getUserData(id: String) : User?
}