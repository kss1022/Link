package com.example.link.widget.adapter.model

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.link.model.CellType
import com.example.link.model.Model
import com.example.link.model.Model.Companion.DIFF_UTIL
import com.example.link.util.resource.ResourceProvider
import com.example.link.widget.adapter.model.listener.ModelAdapterListener
import com.example.link.widget.adapter.model.viewholder.ModelViewHolder

class ModelRecyclerViewAdapter<M : Model>(
    private var modelList: List<Model>,
    private val resourcesProvider: ResourceProvider,
    private val adapterListener: ModelAdapterListener,
) : ListAdapter<Model, ModelViewHolder<M>>(DIFF_UTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModelViewHolder<M> =
        ModelViewMapper.map(
            parent,
            CellType.values()[viewType],
            resourcesProvider
        )


    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: ModelViewHolder<M>, position: Int) {
        with(holder){
            bindData(modelList[position] as M)
            holder.bindViews(modelList[position] as M, adapterListener)
        }
    }


    override fun getItemCount(): Int = modelList.size


    override fun submitList(list: List<Model>?) {
        list?.let {
            modelList = it
        }
        super.submitList(list)
    }
}

