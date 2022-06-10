package com.example.link.model

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil

abstract class Model (
    open val id : String,
    open val cellType: CellType
){
    companion object{
        val DIFF_UTIL =  object : DiffUtil.ItemCallback<Model>(){
            override fun areItemsTheSame(oldItem: Model, newItem: Model): Boolean {
                return oldItem.id == newItem.id  && oldItem.cellType == newItem.cellType
            }


            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: Model, newItem: Model): Boolean {
                return  oldItem == newItem
            }
        }
    }
}

