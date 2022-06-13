package com.example.link.data.repository

import com.example.link.data.api.FireStoreApi
import com.example.link.data.entity.RecordEntity
import com.example.link.model.RecordModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject


class RecordRepositoryImpl @Inject constructor(
    private val ioDispatcher: CoroutineDispatcher,
    private val fireStoreApi: FireStoreApi
) : RecordRepository {

    override suspend fun addRecordToday(id: String): RecordModel = withContext(ioDispatcher){
        fireStoreApi.addRecordToday(id).let {
            RecordModel(
                day= it.day!!,
                week = it.week!!,
                meal = it.meal!!,
                snack = it.snack!!,
                walkCount = it.walkCount!!,
                walkLength = it.walkLength!!,
                walkTime = it.walkTime!!,
                shower =it.shower!!
            )
        }
    }

    override suspend fun getRecordToday(id: String): RecordModel? = withContext(ioDispatcher) {
        fireStoreApi.getRecordToday(id)?.let {
            RecordModel(
                day= it.day!!,
                week = it.week!!,
                meal = it.meal!!,
                snack = it.snack!!,
                walkCount = it.walkCount!!,
                walkLength = it.walkLength!!,
                walkTime = it.walkTime!!,
                shower =it.shower!!
            )
        } ?: kotlin.run {
            null
        }
    }

    override suspend fun getRecordWeek(id: String): List<RecordModel> = withContext(ioDispatcher) {
        fireStoreApi.getRecordWeek(id).map {
            RecordModel(
                day= it.day!!,
                week = it.week!!,
                meal = it.meal!!,
                snack = it.snack!!,
                walkCount = it.walkCount!!,
                walkLength = it.walkLength!!,
                walkTime = it.walkTime!!,
                shower =it.shower!!
            )
        }
    }

    override suspend fun getRecordMonth(id: String): List<RecordModel> = withContext(ioDispatcher) {
            TODO("Not yet implemented")
        }

    override suspend fun updateShower(id: String) = withContext(ioDispatcher) {
        fireStoreApi.updateShower(id)
    }

    override suspend fun updateMeal(id: String, list: MutableList<Int>) = withContext(ioDispatcher){
        fireStoreApi.updateMeal(id, list)
    }

    override suspend fun updateSnack(id: String, list: MutableList<Int>) = withContext(ioDispatcher){
        fireStoreApi.updateSnack(id, list)
    }

    override suspend fun updateWalk(
        id: String,
        walkCountList: MutableList<Int>,
        walkLengthList: MutableList<Double>,
        walkTimeList: MutableList<Int>
    ) = withContext(ioDispatcher) {
        fireStoreApi.updateWalk(id, walkCountList, walkLengthList, walkTimeList)
    }


}