package com.example.link.data.entity

import com.google.firebase.firestore.DocumentId

data class RecordEntity (
    @DocumentId val id: String?= null,
    val day : Int? = null,
    val week : Int? = null,
    val meal : List<Int>? = null,
    val snack : List<Int>? = null,
    val walkCount : List<Int>? = null,
    val walkLength : List<Double>? = null,
    val walkTime : List<Int>? = null,
    val shower : Boolean?=null,
)