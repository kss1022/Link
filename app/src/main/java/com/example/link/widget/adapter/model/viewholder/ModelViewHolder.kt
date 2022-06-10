package com.example.link.widget.adapter.model.viewholder

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.link.model.Model
import com.example.link.util.resource.ResourceProvider
import com.example.link.widget.adapter.model.listener.ModelAdapterListener

abstract class ModelViewHolder<M : Model>(
    binding: ViewBinding,
    protected val resourcesProvider: ResourceProvider
) : RecyclerView.ViewHolder(binding.root) {

    abstract fun reset()

    open fun bindData(model: M) {
        reset()
    }

    abstract fun bindViews(model: M, adapterListener: ModelAdapterListener)
}