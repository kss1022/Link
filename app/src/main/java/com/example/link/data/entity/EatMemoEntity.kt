package com.example.link.data.entity


data class EatMemoEntity(
    val id: String,
    val userName: String,
    val profileNum: Int,
    val memo: String,
    val time: Long,
    val eatType: String,
    val eatAmount: String,
)