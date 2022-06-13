package com.example.link.ui.start.login

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.link.data.repository.PetRepository
import com.example.link.ui.base.BaseViewModel
import com.example.link.util.ext.toReadableDateString
import com.example.link.util.lifecycle.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.*
import javax.inject.Inject

@HiltViewModel
class LoginPatViewModel @Inject constructor(
    app: Application,
    override val savedStateHandle: SavedStateHandle,
    private val petRepository: PetRepository
) : BaseViewModel(app) {

    val patAge = SingleLiveEvent<Pair<Int, Int>?>(100)
    val nextClickEvent = SingleLiveEvent<Boolean>(100)

    val isMail : LiveData<Boolean> = savedStateHandle.getLiveData(KEY_IS_MAIL, null)
    val name : LiveData<String> = savedStateHandle.getLiveData(KEY_NAME, "")
    val type : LiveData<String> = savedStateHandle.getLiveData(KEY_TYPE, "")
    val birthday : LiveData<String> = savedStateHandle.getLiveData(KEY_BIRTHDAY, "")
    val weight : LiveData<Float> = savedStateHandle.getLiveData(KEY_WEIGHT, 0f)

    companion object {
        const val KEY_IS_MAIL = "is_mail"
        const val KEY_NAME = "name"
        const val KEY_TYPE = "type"
        const val KEY_BIRTHDAY = "birthday"
        const val KEY_WEIGHT = "wight"
    }


    fun clickNext() = viewModelScope.launch {
        val checked = checkInputData()

        if(checked.not()){
            nextClickEvent.value = false
            Log.e("Check Not", "${isMail.value}, ${name.value}, ${type.value}, ${birthday.value} ,${weight.value}")
            return@launch
        }

        petRepository.savePetData(
            name.value!!,
            isMail.value!!,
            type.value!!,
            checkInputBirthDayDate(birthday.value!!)!!,
            weight.value!!,
        )
        nextClickEvent.value = true
    }




    /**
     * Set Value
     */

    fun setMail(boolean: Boolean) {
        savedStateHandle.set(KEY_IS_MAIL, boolean)
    }

    fun setName(str: String) {
        savedStateHandle.set(KEY_NAME, str)
    }

    fun setType(str: String) {
        savedStateHandle.set(KEY_TYPE, str)
    }

    fun setBirthday(str: String) {
        if (str.length == 10) {
            val date = checkInputBirthDayDate(str)

            // 입력한 날자가 잘못된  경우 check
            if (date == null) {
                Log.e("Pat BirthDay", "Input data Error")
                setAgeError()
                return
            } else {
                checkAge(date)
            }
        } else {
            //아직 다 입력받지 않음
            patAge.value = null
        }

        savedStateHandle.set(KEY_BIRTHDAY, str)
    }


    //todo weigth를 소수점으로 받는다.
    fun setWeight(str: String) {
        if (checkStringIsNum(str)) {
            savedStateHandle.set(KEY_WEIGHT, str.toInt())
        } else {
            //숫자가 아닌 값이 들어가있음
            Log.e("Weight Error", str)
        }
    }


    /**
     * Check Data
     */

    private fun checkInputData() : Boolean{
        return (name.value != "" && isMail.value != null && type.value != ""
                && patAge.value != null && patAge.value!!.first != -1
                &&  weight.value!! > 0.0f)
    }



    private fun checkAge(date: List<Int>) {
        try {
            val today = Date(System.currentTimeMillis()).toReadableDateString().split(".")

            var year = today[0].toInt() - date[0]
            var month = today[1].toInt() - date[1]
            val day = today[2].toInt() - date[2]

            // 펫의 생일 날자가 오늘의 날자보다 큰 경우 check
            if (year < 0) {
                setAgeError()
            } else if (year == 0 && month < 0) {
                setAgeError()
            } else if (year == 0 && month == 0 && day < 0) {
                setAgeError()
            } else {
                if (day < 0) {
                    month--
                }
                if (month < 0) {
                    year--
                    month += 12
                }

                patAge.value = year to month
            }
        } catch (e: Exception) {
            //error
            setAgeError()
        }
    }


    private fun checkInputBirthDayDate(str: String): List<Int>? {
        return try {
            val date = str.split("-")
            val year = date[0].toInt()
            val month = date[1].toInt()
            val day = date[2].toInt()

            return if ((year in 1900..2100) && (month in 1..12) && (day in 1..31)) {
                listOf<Int>(year, month, day)
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }



    private fun checkStringIsNum(str: String): Boolean {
        if(str == "") return false
        var temp: Char

        var result = true

        for (i in 0 until str.length) {
            temp = str.elementAt(i)
            if (temp.toInt() < 48 || temp.toInt() > 57) {
                result = false
            }
        }

        return result
    }


    private fun setAgeError() {
        patAge.value = -1 to -1
    }
}