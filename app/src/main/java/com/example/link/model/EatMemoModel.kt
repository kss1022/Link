package com.example.link.model

import androidx.annotation.DrawableRes


data class EatMemoModel(
    override val id: String,
    override val cellType: CellType = CellType.EAT_MEMO,
    val userName: String,
    @DrawableRes val userProfileImage: Int,
    val memo: String,
    val time: String,
    val eatType: String,
    val eatAmount: String,
) : Model(id, cellType)