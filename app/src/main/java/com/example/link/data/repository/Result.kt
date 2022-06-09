package com.example.link.data.repository

sealed class Result {

    data class Success<T>(
        val data: T? = null
    ) : Result()


    data class Error(
        val e: Throwable
    ) : Result()
}