package com.example.link.data.repository

import androidx.annotation.DrawableRes
import com.example.link.data.api.FireStoreApi
import com.example.link.model.User
import com.example.link.ui.start.login.ProfileCategory
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

    override suspend fun saveUserId(userId: String, email: String, token: String) = withContext(ioDispatcher)  {
        fireStoreApi.saveUserId(userId, email)
        preferenceManager.setUserId(userId)
        preferenceManager.setUserToken(token)
    }

    override suspend fun saveExistUserData(userId: String, token: String) = withContext(ioDispatcher){
        preferenceManager.setUserId(userId)
        preferenceManager.setUserToken(token)
    }

    override suspend fun checkUserIsExist(userId: String): Boolean = withContext(ioDispatcher) {
        fireStoreApi.checkUserIsExist(userId)
    }

    override suspend fun getUserData(id: String): User? = withContext(ioDispatcher){
        fireStoreApi.getUserData(id)
    }

    override suspend fun getUserProfileNum(userId: String): Int {
        val num = fireStoreApi.getUserProfileNum(userId)
        val profileId = getProfileId(num)

        preferenceManager.setProfileId(profileId)
        return profileId
    }



    override suspend fun saveUserNameWithProfileNum(name: String, @DrawableRes resId : Int, num: Int) = withContext(ioDispatcher){
        val id =  preferenceManager.getUserId()
        preferenceManager.setProfileId(resId)

        if(id != null){
            fireStoreApi.saveUserNameWithProfileNum( id =id, name = name, num =num)
        }
    }


    private fun getProfileId(num: Int): Int {
        val category = ProfileCategory.values()
        return  category[num].imageId
    }
}