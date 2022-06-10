package com.example.link.model

import com.google.firebase.firestore.DocumentId


data class User (
    val id : String? = null,
    val email: String?= null,
    val name : String?= null,
    val profileNum : Int?= null
)