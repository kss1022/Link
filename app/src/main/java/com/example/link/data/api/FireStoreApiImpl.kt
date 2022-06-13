package com.example.link.data.api

import com.example.link.data.entity.PetEntity
import com.example.link.data.entity.RecordEntity
import com.example.link.model.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.tasks.await
import java.util.*
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

        return user
    }




    private val calendar = Calendar.getInstance()
    private val year = calendar.get(Calendar.YEAR) //현재 년도
    private val month = calendar.get(Calendar.MONTH)  // 현재 월(1월 -> 0)
    private val day = calendar.get(Calendar.DATE)  // // 현재 월의 날짜
    private val week = calendar.get(Calendar.WEEK_OF_YEAR) // 현재 년도의 몇째 주

    override suspend fun addRecordToday(id: String) : RecordEntity{
        val today = RecordEntity(
            day = day,
            week =week,
            meal= emptyList(),
            snack= emptyList(),
            walkCount = listOf(),
            walkLength = listOf(),
            walkTime = listOf(),
            shower = false,
        )

       fireStore.collection(PET).document(id).collection("${year}.${month + 1}").document(day.toString())
            .set( today )

        /**
        fireStore.collection(PET).document(id).collection("${year}.${month + 1}").document((day-1).toString())
            .set( RecordEntity(
                day = day-1,
                week =week,
                meal= emptyList(),
                snack= emptyList(),
                walkCount = listOf(),
                walkLength = listOf(),
                walkTime = listOf(),
                shower = false,
            ))

        fireStore.collection(PET).document(id).collection("${year}.${month + 1}").document((day-2).toString())
            .set( RecordEntity(
                day = day-2,
                week =week,
                meal= emptyList(),
                snack= emptyList(),
                walkCount = listOf(),
                walkLength = listOf(),
                walkTime = listOf(),
                shower = false,
            ))

        fireStore.collection(PET).document(id).collection("${year}.${month + 1}").document((day-3).toString())
            .set( RecordEntity(
                day = day-3,
                week =week,
                meal= emptyList(),
                snack= emptyList(),
                walkCount = listOf(),
                walkLength = listOf(),
                walkTime = listOf(),
                shower = false,
            ))

        fireStore.collection(PET).document(id).collection("${year}.${month + 1}").document((day-4).toString())
            .set( RecordEntity(
                day = day-4,
                week =week,
                meal= emptyList(),
                snack= emptyList(),
                walkCount = listOf(),
                walkLength = listOf(),
                walkTime = listOf(),
                shower = false,
            ))

        fireStore.collection(PET).document(id).collection("${year}.${month + 1}").document((day-5).toString())
            .set( RecordEntity(
                day = day-5,
                week =week,
                meal= emptyList(),
                snack= emptyList(),
                walkCount = listOf(),
                walkLength = listOf(),
                walkTime = listOf(),
                shower = false,
            ))



         */

        return today
    }

    override suspend fun getRecordToday(id: String): RecordEntity? {
        val todayRecord = fireStore.collection(PET).document(id).collection("${year}.${month + 1}").document(day.toString())
            .get()
            .await()
            .toObject<RecordEntity>()


        return todayRecord
    }

    override suspend fun getRecordWeek(id: String): List<RecordEntity> {
       val weekRecords =  fireStore.collection(PET).document(id).collection("${year}.${month + 1}")
            .whereEqualTo(WEEK, week)
            .get()
            .await()
            .map {
                it.toObject<RecordEntity>()
            }

        return weekRecords
    }

    override suspend fun getRecordMonth(id: String): List<RecordEntity> {
        TODO("Not yet implemented")
    }


    override suspend fun updateRecordToday(id: String, recordEntity: RecordEntity) {
        fireStore.collection(PET).document(id).collection("${year}.${month + 1}").document(day.toString())
    }

    override suspend fun updateEating(id: String, isEating: Boolean, amount: String) {
        fireStore.collection(PET).document(id).collection("${year}.${month + 1}").document(day.toString())
    }


    override suspend fun updateWalk(
        id: String,
        count: MutableList<Int>,
        length: MutableList<Double>,
        time: MutableList<Int>
    ) {
        val document = fireStore.collection(PET).document(id).collection("${year}.${month + 1}").document(day.toString())
//        document.update(WALK_COUNT, count , WALK_LENGTH, length ,WALK_TIME , time)
        document.update(WALK_COUNT, count)
        document.update(WALK_LENGTH,length)
        document.update(WALK_TIME,time)
    }

    override suspend fun updateShower(id: String) {
        fireStore.collection(PET).document(id).collection("${year}.${month + 1}").document(day.toString())
            .update(SHOWER, true)
    }

    override suspend fun updateMeal(id: String, list: MutableList<Int>) {
        fireStore.collection(PET).document(id).collection("${year}.${month + 1}").document(day.toString())
            .update(MEAL , list)
    }

    override suspend fun updateSnack(id: String, list: MutableList<Int>) {
        fireStore.collection(PET).document(id).collection("${year}.${month + 1}").document(day.toString())
            .update(SNACK , list)
    }


    companion object {
        const val USER = "user"
        const val EMAIL = "email"
        const val NAME = "name"
        const val PROFILE = "profileNum"
        const val PET = "pet"

        const val WEEK = "week"
        const val DAY = "day"
        const val SHOWER = "shower"
        const val WALK_LENGTH = "walkLength"
        const val WALK_TIME = "walkTime"
        const val WALK_COUNT = "walkCount"
        const val SNACK = "snack"
        const val MEAL = "meal"
    }
}
