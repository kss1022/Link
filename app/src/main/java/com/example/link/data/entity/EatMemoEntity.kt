package com.example.link.data.entity

import androidx.annotation.DrawableRes
import com.example.link.model.CellType


data class EatMemoEntity(
    val id: String,
    val userName: String,
    val profileNum: Int,
    val memo: String,
    val time: Long,
    val eatType: String,
    val eatAmount: String,
)