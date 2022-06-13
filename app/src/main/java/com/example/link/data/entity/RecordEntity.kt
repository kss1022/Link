package com.example.link.data.entity

import com.google.firebase.firestore.DocumentId

data class RecordEntity (
    @DocumentId val id: String?= null,
    val day : Int? = null,
    val week : Int? = null,
    val meal : Int? = null,
    val mealCount : Int? = null,
    val snack : Int? = null,
    val snackCount : Int? = null,
    val walkStep : Int? = null,
    val walkLength : Double? = null,
    val walkTime : Int? = null,
    val walkCount : Int? = null,
    val shower : Boolean?=null,
)