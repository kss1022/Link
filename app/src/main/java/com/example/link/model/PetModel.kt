package com.example.link.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

data class PetModel (
    val name : String,
    val isMail : Boolean,
    val type : String,
    val birthDay : List<Int>,
    val weight : Float
)