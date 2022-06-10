package com.example.link.model

import androidx.annotation.DrawableRes


data class MemoModel (
    override val id: String,
    override val cellType: CellType = CellType.MEMO,
    val userName : String,
    @DrawableRes val userProfileImage : Int,
    val memo : String
) : Model(id, cellType)