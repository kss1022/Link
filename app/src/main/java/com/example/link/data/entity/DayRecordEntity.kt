package com.example.link.data.entity

import com.google.firebase.firestore.DocumentId

data class DayRecordEntity (
    @DocumentId val id: String?= null,
    val day : String? = null,
    val week : Int? = null,
    val meal : List<Int>? = null,
    val snack : List<Int>? = null,
    val walk : List<Int>? = null,
    val walkTime : List<Int>? = null,
    val shower : Boolean?=null,
)