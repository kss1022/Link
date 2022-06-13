package com.example.link.data.api

import com.example.link.data.entity.RecordEntity
import com.example.link.data.entity.PetEntity
import com.example.link.model.User

interface FireStoreApi {
    //DefaultData
    suspend fun saveUserId(id: String, email: String)

    suspend fun saveUserNameWithProfileNum(id :String, name : String, num : Int)

    suspend fun savePetData(petEntity: PetEntity, userId: String)

    suspend fun getPetData(userId: String) : PetEntity?

    suspend fun checkUserIsExist(userId: String) : Boolean

    suspend fun checkPetIsExist(userId: String) : Boolean

    suspend fun getUserProfileNum(userId: String) : Int

    suspend fun getUserData(id: String) : User?

    suspend fun updateUserName(id : String,userName: String)


    //Record

    suspend fun addRecordToday(id:String) : RecordEntity

    suspend fun addRecordMonth(id:String) : RecordEntity

    suspend fun addRecordYear(id:String) : RecordEntity

    suspend fun getRecordToday(id : String) : RecordEntity?

    suspend fun getRecordWeek(id : String) : List<RecordEntity>

    suspend fun getRecordMonth(id : String) : RecordEntity?

    suspend fun getRecordYear(id : String) : RecordEntity?

    suspend fun updateRecordToday(id: String, recordEntity: RecordEntity)

    suspend fun updateShower( id : String)

    suspend fun updateMeal(id: String, meal: Long)

    suspend  fun updateSnack(id: String, snack: Long)

    suspend fun updateWalk(id: String, step: Int, length: Double, time: Int)


}