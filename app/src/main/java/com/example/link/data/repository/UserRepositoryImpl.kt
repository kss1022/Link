package com.example.link.data.repository

import com.example.link.data.api.FireStoreApi
import com.example.yourchoice.data.preference.PreferenceManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val ioDispatcher: CoroutineDispatcher,
    private val fireStoreApi: FireStoreApi,
    private val preferenceManager: PreferenceManager
) : UserRepository {

    override suspend fun saveUserId(userId: String, email: String): Boolean = withContext(ioDispatcher) {
            fireStoreApi.saveUserId(userId, email)
            preferenceManager.setUserId(userId)
            true
        }

    override suspend fun saveUserNameWithProfileNum(name: String, num: Int) = withContext(ioDispatcher){
        val id =  preferenceManager.getUserId()

        if(id != null){
            fireStoreApi.saveUserNameWithProfileNum( id =id, name = name, num =num)
        }
    }

    override suspend fun loginInWithEmail(email: String, password: String): Boolean =
        withContext(ioDispatcher) {
            TODO("Not yet implemented")
        }
}