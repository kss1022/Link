package com.example.link.data.repository

import com.example.link.model.RecordModel

interface RecordRepository {
    suspend fun addRecordToday(id: String) : RecordModel

    suspend fun addRecordMonth(id: String): RecordModel

    suspend fun addRecordYear(id: String): RecordModel

    suspend fun getRecordToday(id : String) : RecordModel?

    suspend fun getRecordWeek(id : String) : List<RecordModel>

    suspend fun getRecordMonth(id : String) : RecordModel?

    suspend fun getRecordYear(id: String) : RecordModel?

    suspend fun updateShower(id : String)

    suspend fun updateMeal(id: String, meal: Long)

    suspend fun updateSnack(id: String, snack: Long)

    suspend fun updateTodayWalk(id: String, step: Int, length: Double, time: Int)
}