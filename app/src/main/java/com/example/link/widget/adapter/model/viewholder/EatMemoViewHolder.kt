package com.example.link.widget.adapter.model.viewholder

import android.annotation.SuppressLint
import com.bumptech.glide.Glide
import com.example.link.databinding.ViewholderEatMemoBinding
import com.example.link.model.EatMemoModel
import com.example.link.util.resource.ResourceProvider
import com.example.link.widget.adapter.model.listener.MemoModelAdapterListener
import com.example.link.widget.adapter.model.listener.ModelAdapterListener

class EatMemoViewHolder(
    private val binding: ViewholderEatMemoBinding,
    private val resourceProvider: ResourceProvider
) : ModelViewHolder<EatMemoModel>(binding , resourceProvider) {

    override fun reset() = Unit

    @SuppressLint("SetTextI18n")
    override fun bindData(model: EatMemoModel) {
        super.bindData(model)

        binding.nameTextView.text = model.userName
        binding.memoTextView.text = model.memo

        binding.timeTextView.text = "${model.time}시간전"
        binding.eatAmountTextView.text= model.eatAmount
        binding.eatTypeTextView.text= model.eatType

        Glide.with(binding.profileImageView)
            .load(model.userProfileImage)
            .circleCrop()
            .into(binding.profileImageView)
    }


    override fun bindViews(model: EatMemoModel, adapterListener: ModelAdapterListener) {
        if (adapterListener is MemoModelAdapterListener){
            binding.root.setOnClickListener {
                adapterListener.click()
            }
        }
    }
}