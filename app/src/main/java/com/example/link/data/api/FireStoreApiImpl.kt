package com.example.link.data.api

import android.util.Log
import com.example.link.data.entity.PatEntity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FireStoreApiImpl @Inject constructor(
    private val fireStore: FirebaseFirestore
) : FireStoreApi {


    override suspend fun saveUserId(id: String, email : String) {
        val user = hashMapOf(
            "id" to id,
            EMAIL to email
        )

        try {
            fireStore.collection(USER).document(id).set(user)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun saveUserNameWithProfileNum(id: String, name: String, num: Int) {
        try {
            Log.e("dafsdaf" ,"$name , $num")
            fireStore.collection(USER).document(id).apply {
                update(NAME, name)
                update(PROFILE, num )
            }
            Log.e("Update" ,"$name , $num")
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    override suspend fun savePetData(patEntity: PatEntity, userId: String) {
        fireStore.collection(PET).document(userId).set(patEntity)
    }


    companion object{
        const val  USER = "user"
        const val  Id = "id"
        const val  EMAIL = "email"
        const val  NAME = "name"
        const val  PROFILE = "profileNum"

        const val  PET="pet"
    }
}
