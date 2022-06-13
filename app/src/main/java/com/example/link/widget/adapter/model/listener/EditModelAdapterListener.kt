package com.example.link.widget.adapter.model.listener

import com.example.link.model.EatMemoModel

interface EditModelAdapterListener : ModelAdapterListener{
    fun clickEditButton( editModel : EatMemoModel)
}