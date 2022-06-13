package com.example.link.data.api

import com.example.link.data.entity.RecordEntity
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


    suspend fun addRecordToday(id:String) : RecordEntity

    suspend fun getRecordToday(id : String) : RecordEntity?

    suspend fun getRecordWeek(id : String) : List<RecordEntity>

    suspend fun getRecordMonth(id : String) : List<RecordEntity>

    suspend fun updateRecordToday(id: String, recordEntity: RecordEntity)

    suspend fun updateEating( id : String, isEating: Boolean, amount: String)

    suspend fun updateShower( id : String)

    suspend fun updateMeal(id: String, list: MutableList<Int>)

    suspend  fun updateSnack(id: String, list: MutableList<Int>)

    suspend fun updateWalk(id: String, count: MutableList<Int>, length: MutableList<Double>, time: MutableList<Int>)
}