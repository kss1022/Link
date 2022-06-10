package com.example.link.widget.adapter.model

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.link.databinding.ViewholderMemoBinding
import com.example.link.model.CellType
import com.example.link.model.Model
import com.example.link.util.resource.ResourceProvider
import com.example.link.widget.adapter.model.viewholder.CommentViewHolder
import com.example.link.widget.adapter.model.viewholder.ModelViewHolder

object ModelViewMapper {

    @Suppress("UNCHECKED_CAST")
    fun <M : Model> map(
        parent: ViewGroup,
        cellType: CellType,
        resourcesProvider: ResourceProvider
    ): ModelViewHolder<M> {

        val viewHolder = when (cellType) {
            CellType.MEMO -> {
                CommentViewHolder(
                    ViewholderMemoBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                    resourcesProvider
                )
            }
        }

        return viewHolder as ModelViewHolder<M>
    }

}