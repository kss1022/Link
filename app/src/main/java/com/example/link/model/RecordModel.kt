package com.example.link.model

data class RecordModel (
    val day : Int,
    val week : Int,
    val meal : List<Int>,
    val snack : List<Int>,
    val walkCount : List<Int>,
    val walkLength : List<Double>,
    val walkTime : List<Int>,
    val shower : Boolean
)