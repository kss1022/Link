package com.example.link.data.repository

interface PetRepository {

    suspend fun savePetData(
        name: String,
        isMail: Boolean,
        type: String,
        birthday: List<Int>,
        wight: Int
    )
}