package com.example.link.data.api

import com.example.link.data.entity.PatEntity

interface FireStoreApi {

    suspend fun saveUserId(id: String, email: String)

    suspend fun saveUserNameWithProfileNum(id :String, name : String, num : Int)

    suspend fun savePetData(patEntity: PatEntity, userId: String)

}