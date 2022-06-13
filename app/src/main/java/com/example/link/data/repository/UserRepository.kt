package com.example.link.data.repository

import androidx.annotation.DrawableRes
import com.example.link.data.entity.EatMemoEntity
import com.example.link.model.User

interface UserRepository {
    suspend fun saveUserId(userId : String , email: String) : Boolean

    suspend fun saveUserId(userId : String , email: String, token : String)

    suspend fun saveExistUserData(value: String, value1: String)

    suspend fun checkUserIsExist(userId: String) : Boolean

    suspend fun getUserData(id: String): User?

    suspend fun getUserProfileNum(userId: String) : Int

    suspend fun saveUserNameWithProfileNum(name : String, @DrawableRes resId : Int, num : Int )

    suspend fun updateUserName(id : String,userName: String)
}