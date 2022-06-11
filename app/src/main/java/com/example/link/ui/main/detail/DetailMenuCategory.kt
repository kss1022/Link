package com.example.link.ui.main.detail

import androidx.annotation.StringRes
import com.example.link.R

enum class DetailMenuCategory(
    @StringRes val categoryNameId: Int,

){
    ALL(R.string.category_all),
    MEAL(R.string.category_meal),
    WALK(R.string.category_walk),
    SLEEP(R.string.category_sleep)
}


