package com.example.link.ui.main

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.link.data.repository.PetRepository
import com.example.link.data.repository.RecordRepository
import com.example.link.data.repository.UserRepository
import com.example.link.model.PetModel
import com.example.link.model.RecordModel
import com.example.link.model.User
import com.example.link.ui.base.BaseViewModel
import com.example.link.util.lifecycle.SingleLiveEvent
import com.example.yourchoice.data.preference.PreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject


@HiltViewModel
class MainSharedViewModel @Inject constructor(
    app: Application,
    override val savedStateHandle: SavedStateHandle,
    private val userRepository: UserRepository,
    private val petRepository: PetRepository,
    private val recordRepository: RecordRepository,
    private val preferenceManager: PreferenceManager
) : BaseViewModel(app) {

    val userId: LiveData<String> = savedStateHandle.getLiveData(KEY_USER_ID, "")

    val petModel = MutableLiveData<PetModel>()
    val userModel = MutableLiveData<User>()

    val weekRecord = MutableLiveData<List<RecordModel>>()
    val todayRecord = MutableLiveData<RecordModel>()

    val updateEvent = SingleLiveEvent<Unit>()

    private val today = Calendar.getInstance().get((Calendar.DATE))

    companion object {
        const val KEY_USER_ID = "user_id"
    }

    fun fetchData() = viewModelScope.launch {
        val id = preferenceManager.getUserId()
        savedStateHandle.set(KEY_USER_ID, id)

        id?.let { _ ->
            val user = userRepository.getUserData(id)
            val pet = petRepository.getPetData(id)
            val weekList = recordRepository.getRecordWeek(id)

            weekRecord.value = weekList
            val today = weekList.find {
                it.day == today
            }

            if (today == null) {
                todayRecord.value = recordRepository.addRecordToday(id)
            } else {
                todayRecord.value = today!!
            }


            pet?.let {
                petModel.value = it
            } ?: {
                //get Pet Data error
            }

            user?.let {
                userModel.value = it
            } ?: {
                //get User Data error
            }

        } ?: kotlin.run {
            //user id error
        }
    }


    fun updateShower() = viewModelScope.launch {
        recordRepository.updateShower(userId.value!!)

        updateData()
    }


    fun saveEating(isEating: Boolean, amount: Int) = viewModelScope.launch {
        if (isEating) {
            val list = todayRecord.value?.meal?.toMutableList()!!
            list.add(amount)

            recordRepository.updateMeal(userId.value!!, list)

        } else {
            val list = todayRecord.value?.snack?.toMutableList()!!
            list.add(amount)

            recordRepository.updateSnack(userId.value!!, list)
        }

        updateData()
    }


    fun saveWalk(count: Int, length: Double, time: Int) = viewModelScope.launch {
        val walkCountList = todayRecord.value!!.walkCount.toMutableList()
        walkCountList.add(count)

        val walkLengthList = todayRecord.value!!.walkLength.toMutableList()
        walkLengthList.add(length)

        val walkTimeList = todayRecord.value!!.walkTime.toMutableList()
        walkTimeList.add(time)

        recordRepository.updateWalk(userId.value!!, walkCountList, walkLengthList, walkTimeList)

        updateData()
    }


    private fun updateData() = viewModelScope.launch {
        val weekList = recordRepository.getRecordWeek(userId.value!!)

        weekRecord.value = weekList

        val today = weekList.find {
            it.day == today
        }
        todayRecord.value = today!!

        updateEvent.call()
    }
}