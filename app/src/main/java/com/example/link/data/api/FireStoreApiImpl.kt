package com.example.link.data.api

import com.example.link.data.entity.PetEntity
import com.example.link.model.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FireStoreApiImpl @Inject constructor(
    private val fireStore: FirebaseFirestore
) : FireStoreApi {


    override suspend fun saveUserId(id: String, email: String) {
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
            fireStore.collection(USER).document(id).apply {
                update(NAME, name)
                update(PROFILE, num)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun savePetData(petEntity: PetEntity, userId: String) {
        fireStore.collection(PET).document(userId).set(petEntity)
    }

    override suspend fun getPetData(userId: String): PetEntity? {
        val pet = fireStore.collection(PET).document(userId)
            .get()
            .await()
            .toObject<PetEntity>()
        return pet
    }

    override suspend fun checkUserIsExist(userId: String): Boolean {
        val user = fireStore.collection(USER).document(userId)
            .get()
            .await()

        return user.exists()
    }

    override suspend fun checkPetIsExist(userId: String): Boolean {
        val pet = fireStore.collection(USER).document(userId)
            .get()
            .await()

        return pet.exists()
    }


    override suspend fun getUserProfileNum(userId: String): Int =
         fireStore.collection(USER).document(userId)
           .get()
           .await()
           .get(PROFILE).toString().toInt()

    override suspend fun getUserData(userId: String): User? {
        val user = fireStore.collection(USER).document(userId)
            .get()
            .await()
            .toObject<User>()

        return  user
    }


    companion object {
        const val USER = "user"
        const val Id = "id"
        const val EMAIL = "email"
        const val NAME = "name"
        const val PROFILE = "profileNum"

        const val PET = "pet"
    }
}
