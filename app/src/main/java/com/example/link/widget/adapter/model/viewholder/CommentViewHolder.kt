package com.example.link.widget.adapter.model.viewholder

import com.bumptech.glide.Glide
import com.example.link.R
import com.example.link.databinding.ViewholderMemoBinding
import com.example.link.model.MemoModel
import com.example.link.util.resource.ResourceProvider
import com.example.link.widget.adapter.model.listener.MemoModelAdapterListener
import com.example.link.widget.adapter.model.listener.ModelAdapterListener

class CommentViewHolder(
    private val binding: ViewholderMemoBinding,
    private val resourceProvider: ResourceProvider
) : ModelViewHolder<MemoModel>(binding , resourceProvider) {

    override fun reset() = Unit

    override fun bindData(model: MemoModel) {
        super.bindData(model)

        binding.nameTextView.text = model.userName
        binding.memoTextView.text = model.memo


        Glide.with(binding.profileImageView)
            .load(model.userProfileImage)
            .circleCrop()
            .into(binding.profileImageView)
    }


    override fun bindViews(model: MemoModel, adapterListener: ModelAdapterListener) {
        if (adapterListener is MemoModelAdapterListener){
            binding.root.setOnClickListener {
                adapterListener.click()
            }
        }
    }
}