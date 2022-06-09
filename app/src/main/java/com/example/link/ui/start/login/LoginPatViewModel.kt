package com.example.link.ui.start.login

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.example.link.ui.base.BaseViewModel
import com.example.link.util.ext.toReadableDateString
import com.example.link.util.lifecycle.SingleLiveEvent
import java.lang.Exception
import java.util.*

class LoginPatViewModel(
    app: Application, override val savedStateHandle: SavedStateHandle
) : BaseViewModel(app) {

    val patAge = SingleLiveEvent<Pair<Int, Int>?>(100)
    val nextClickEvent = SingleLiveEvent<Boolean>(100)

    val isMail = savedStateHandle.getLiveData<Boolean?>(KEY_IS_MAIL, null)
    val name = savedStateHandle.getLiveData<String>(KEY_NAME, "")
    val type = savedStateHandle.getLiveData<String>(KEY_TYPE, "")
    val birthday = savedStateHandle.getLiveData<String>(KEY_BIRTHDAY, "")
    val weight = savedStateHandle.getLiveData<String>(KEY_WEIGHT, "")

    companion object {
        const val KEY_IS_MAIL = "is_mail"
        const val KEY_NAME = "name"
        const val KEY_TYPE = "type"
        const val KEY_BIRTHDAY = "birthday"
        const val KEY_WEIGHT = "wight"
    }


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
        savedStateHandle.set(KEY_BIRTHDAY, str)
        checkAge(str)
    }


    fun setWeight(str: String) {
        savedStateHandle.set(KEY_WEIGHT, str)
    }


    private fun checkAge(str: String) {
        if (str.length == 10) {
            try {
                val date = str.split("-")
                val today = Date(System.currentTimeMillis()).toReadableDateString().split(".")


                var year = today[0].toInt() - date[0].toInt()
                var month = today[1].toInt() - date[1].toInt()
                val day = today[2].toInt() - date[2].toInt()

                if (year < 0) {
                    //error
                    patAge.value = -1 to -1
                } else if (year == 0 && month < 0) {
                    //error
                    patAge.value = -1 to -1
                } else if (year == 0 && month == 0 && day < 0) {
                    //error
                    patAge.value = -1 to -1
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
                patAge.value = -1 to -1
            }
        } else {
            patAge.value = null
        }
    }

    fun clickNext() {
        nextClickEvent.value = true
    }


}