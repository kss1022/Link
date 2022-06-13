package com.example.link.data.repository

import com.example.link.data.entity.RecordEntity
import com.example.link.model.RecordModel

interface RecordRepository {
    suspend fun addRecordToday(id: String) : RecordModel

    suspend fun getRecordToday(id : String) : RecordModel?

    suspend fun getRecordWeek(id : String) : List<RecordModel>

    suspend fun getRecordMonth(id : String) : List<RecordModel>

    suspend fun updateShower(id : String)

    suspend fun updateMeal(id: String, list: MutableList<Int>)

    suspend fun updateSnack(id: String, list: MutableList<Int>)

    suspend fun updateWalk(id: String, walkCountList: MutableList<Int>, walkLengthList: MutableList<Double>, walkTimeList: MutableList<Int>)

}