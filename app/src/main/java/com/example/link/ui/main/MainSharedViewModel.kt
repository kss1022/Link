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
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
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

    private  val userId: LiveData<String> = savedStateHandle.getLiveData(KEY_USER_ID, "")

    val petModel = MutableLiveData<PetModel>()
    val userModel = MutableLiveData<User>()

    val weekRecord = MutableLiveData<List<RecordModel>>()
    val todayRecord = MutableLiveData<RecordModel>()
    val monthRecord = MutableLiveData<RecordModel>()
    val yearRecord = MutableLiveData<RecordModel>()



    val updateStartEvent = SingleLiveEvent<Unit>()
    val updateEndEvent = SingleLiveEvent<Unit>()

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


            weekRecord.value = recordRepository.getRecordWeek(id)
            val month = recordRepository.getRecordMonth(id)
            val year = recordRepository.getRecordYear(id)


            val today = weekRecord.value!!.find {
                it.day == today
            }

            // if is no Exist
            if (today == null) {
                todayRecord.value = recordRepository.addRecordToday(id)
            } else {
                todayRecord.value = today!!
            }

            if (month == null) {
                monthRecord.value = recordRepository.addRecordMonth(id)
            } else {
                monthRecord.value = month!!
            }

            if (year == null) {
                yearRecord.value = recordRepository.addRecordYear(id)
            } else {
                yearRecord.value = year!!
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

        updateEndEvent.call()
    }


    fun saveShower() = viewModelScope.launch {
        updateStartEvent.call()
        recordRepository.updateShower(userId.value!!)

        updateData()
    }


    fun saveEating(isEating: Boolean, amount: Long) = viewModelScope.launch {
        updateStartEvent.call()
        if (isEating) {
            recordRepository.updateMeal(userId.value!!, amount)
        } else {
            recordRepository.updateSnack(userId.value!!, amount)
        }
        updateData()
    }


    fun saveWalk(step: Int, length: Double, time: Int) = viewModelScope.launch {
        updateStartEvent.call()
        val newStep = todayRecord.value!!.walkCount + step
        val newLength = todayRecord.value!!.walkLength + length
        val newTime = todayRecord.value!!.walkTime + time

        recordRepository.updateTodayWalk(userId.value!!, newStep, newLength, newTime)

        updateData()
    }


    private fun updateData() = viewModelScope.launch {
        val weekList = recordRepository.getRecordWeek(userId.value!!)
        monthRecord.value = recordRepository.getRecordMonth(userId.value!!)
        yearRecord.value = recordRepository.getRecordYear(userId.value!!)

        weekRecord.value = weekList

        val today = weekList.find {
            it.day == today
        }
        todayRecord.value = today!!

        updateEndEvent.call()
    }


    fun updateUserName(userName: String) =viewModelScope.launch{
        userRepository.updateUserName(userId.value!!, userName)
        userModel.value =userRepository.getUserData(userId.value!!)
    }
}