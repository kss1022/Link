package com.example.link.ui.main.detail.fragmentlist

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.example.link.R
import com.example.link.model.EatMemoModel
import com.example.link.model.MemoModel
import com.example.link.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailMealViewModel @Inject constructor(
    app: Application,
    override val savedStateHandle: SavedStateHandle
) : BaseViewModel(app) {


    val memoList = MutableLiveData<List<EatMemoModel>>()

    val menu: LiveData<String> = savedStateHandle.getLiveData(KEY_MEAL_TOP_MENU, DAY)

    companion object {
        const val DAY = "day"
        const val WEEK = "week"
        const val MONTH = "month"
        const val YEAR = "year"

        const val KEY_MEAL_TOP_MENU = "meal_top_menu"
    }


    fun fetchData() {
        memoList.value = listOf<EatMemoModel>(
            EatMemoModel(
                id = "5",
                userName = "엄마",
                userProfileImage = R.drawable.profile13,
                time = "1",
                memo = "이번에 바꾼 사료 좋아함 ! 저녁에는 100g만 줘",
                eatType = "웰스비 어덜트 강아지 유기농 사료",
                eatAmount = "200g"
            ),
            EatMemoModel(
                id = "3",
                userName = "아빠",
                userProfileImage = R.drawable.profile16,
                time = "3",
                memo = "뭉치 다이어트 하자....",
                eatType = "없음",
                eatAmount = "0g"
            ),
            EatMemoModel(
                id = "1",
                userName = "뭉치 누나",
                userProfileImage = R.drawable.profile11,
                time = "5",
                memo = "생식 처음해봤는데 뭉치 너무 좋아한다 앞으로 자주 해주자 ㅜㅜ",
                eatType = "닭고기",
                eatAmount = "300g"
            ),
        )
    }


    fun setTopMenu(menu: String) {
        savedStateHandle.set(KEY_MEAL_TOP_MENU, menu)
    }
}