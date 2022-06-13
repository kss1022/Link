package com.example.link.data.entity

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

data class PetEntity (
    @DocumentId val id: String?= null,
    val name : String? = null,
    val mail : Boolean? = null,
    val type : String? = null,
    val birthDay : List<Int>? = null,
    val weight : Float? = null
)