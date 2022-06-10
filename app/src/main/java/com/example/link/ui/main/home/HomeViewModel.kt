package com.example.link.ui.main.home

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.example.link.R
import com.example.link.model.MemoModel
import com.example.link.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    app: Application,
    override val savedStateHandle: SavedStateHandle,
) : BaseViewModel(app) {

    val memoList = MutableLiveData<List<MemoModel>>()

    fun fetchData() {
        memoList.value = listOf<MemoModel>(
            MemoModel(
                id = "1",
                userName = "엄마",
                userProfileImage = R.drawable.profile13,
                memo = "오늘 저녁 뭉치 밥 주지마\n내일 검강 검진이야"
            ),
            MemoModel(
                id = "2",
                userName = "아빠",
                userProfileImage = R.drawable.profile16,
                memo = "딸 뭉치 사료 주문해줘"
            ),
            MemoModel(
                id = "3",
                userName = "뭉치 누나",
                userProfileImage = R.drawable.profile11,
                memo = "오늘 저녁 뭉치 산책 해줘야해!"
            ),
        )
    }

}