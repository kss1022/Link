package com.example.link.data.repository

import com.example.link.data.api.FireStoreApi
import com.example.link.model.RecordModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject


class RecordRepositoryImpl @Inject constructor(
    private val ioDispatcher: CoroutineDispatcher,
    private val fireStoreApi: FireStoreApi
) : RecordRepository {

    override suspend fun addRecordToday(id: String): RecordModel = withContext(ioDispatcher) {
        fireStoreApi.addRecordToday(id).let {
            RecordModel(
                day = it.day!!,
                week = it.week!!,
                meal = it.meal!!,
                mealCount = it.mealCount!!,
                snack = it.snack!!,
                snackCount = it.snackCount!!,
                walkStep = it.walkStep!!,
                walkLength = it.walkLength!!,
                walkTime = it.walkTime!!,
                walkCount = it.walkCount!!,
                shower = it.shower!!
            )
        }
    }

    override suspend fun addRecordMonth(id: String): RecordModel = withContext(ioDispatcher){
        fireStoreApi.addRecordMonth(id).let {
            RecordModel(
                day = 0,
                week = it.week!!,
                meal = it.meal!!,
                mealCount = it.mealCount!!,
                snack = it.snack!!,
                snackCount = it.snackCount!!,
                walkStep = it.walkStep!!,
                walkLength = it.walkLength!!,
                walkTime = it.walkTime!!,
                walkCount = it.walkCount!!,
                shower = it.shower!!
            )
        }
    }

    override suspend fun addRecordYear(id: String): RecordModel = withContext(ioDispatcher){
        fireStoreApi.addRecordYear(id).let {
            RecordModel(
                day = it.day!!,
                week = it.week!!,
                meal = it.meal!!,
                mealCount = it.mealCount!!,
                snack = it.snack!!,
                snackCount = it.snackCount!!,
                walkStep = it.walkStep!!,
                walkLength = it.walkLength!!,
                walkTime = it.walkTime!!,
                walkCount = it.walkCount!!,
                shower = it.shower!!
            )
        }
    }

    override suspend fun getRecordToday(id: String): RecordModel? = withContext(ioDispatcher) {
        fireStoreApi.getRecordToday(id)?.let {
            RecordModel(
                day = it.day!!,
                week = it.week!!,
                meal = it.meal!!,
                mealCount = it.mealCount!!,
                snack = it.snack!!,
                snackCount = it.snackCount!!,
                walkStep = it.walkStep!!,
                walkLength = it.walkLength!!,
                walkTime = it.walkTime!!,
                walkCount = it.walkCount!!,
                shower = it.shower!!
            )
        } ?: kotlin.run {
            null
        }
    }

    override suspend fun getRecordWeek(id: String): List<RecordModel> = withContext(ioDispatcher) {
        fireStoreApi.getRecordWeek(id).map {
            RecordModel(
                day = it.day!!,
                week = it.week!!,
                meal = it.meal!!,
                mealCount = it.mealCount!!,
                snack = it.snack!!,
                snackCount = it.snackCount!!,
                walkStep = it.walkStep!!,
                walkLength = it.walkLength!!,
                walkTime = it.walkTime!!,
                walkCount = it.walkCount!!,
                shower = it.shower!!
            )
        }
    }

    override suspend fun getRecordMonth(id: String): RecordModel? = withContext(ioDispatcher) {
        fireStoreApi.getRecordMonth(id)?.let {
            RecordModel(
                day = it.day!!,
                week = it.week!!,
                meal = it.meal!!,
                mealCount = it.mealCount!!,
                snack = it.snack!!,
                snackCount = it.snackCount!!,
                walkStep = it.walkStep!!,
                walkLength = it.walkLength!!,
                walkTime = it.walkTime!!,
                walkCount = it.walkCount!!,
                shower = it.shower!!
            )
        } ?: kotlin.run {
            null
        }
    }

    override suspend fun getRecordYear(id: String): RecordModel? = withContext(ioDispatcher){
        fireStoreApi.getRecordYear(id)?.let {
            RecordModel(
                day = it.day!!,
                week = it.week!!,
                meal = it.meal!!,
                mealCount = it.mealCount!!,
                snack = it.snack!!,
                snackCount = it.snackCount!!,
                walkStep = it.walkStep!!,
                walkLength = it.walkLength!!,
                walkTime = it.walkTime!!,
                walkCount = it.walkCount!!,
                shower = it.shower!!
            )
        } ?: kotlin.run {
            null
        }
    }

    override suspend fun updateShower(id: String) = withContext(ioDispatcher) {
        fireStoreApi.updateShower(id)
    }

    override suspend fun updateMeal(id: String, meal: Long) = withContext(ioDispatcher) {
        fireStoreApi.updateMeal(id, meal)
    }

    override suspend fun updateSnack(id: String, snack: Long) = withContext(ioDispatcher) {
        fireStoreApi.updateSnack(id, snack)
    }


    override suspend fun updateTodayWalk(
        id: String,
        step: Int,
        length: Double,
        time: Int
    ) = withContext(ioDispatcher) {
        fireStoreApi.updateWalk(id, step, length, time)
    }

}