package com.example.link.data.repository

interface UserRepository {
    suspend fun saveUserId(userId : String , email: String) : Boolean

    suspend fun saveUserNameWithProfileNum( name : String, num : Int )

    suspend fun loginInWithEmail( email : String, password : String) : Boolean
}